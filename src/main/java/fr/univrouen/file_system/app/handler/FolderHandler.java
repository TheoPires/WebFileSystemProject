package fr.univrouen.file_system.app.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.factory.FileFactory;
import fr.univrouen.file_system.file.folder.Folder;
import fr.univrouen.file_system.file.link.LinkFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of HttpHandler to define application behavior
 * when you use a Folder
 */
public class FolderHandler implements HttpHandler {
    private HttpServer server;
    private FileFactory factory;
    private Map<String, String> params;
    private Map<String, fr.univrouen.file_system.file.File> systemFile;

    public FolderHandler(HttpServer server, FileFactory factory,
                         Map<String, fr.univrouen.file_system.file.File> systemFile) {
        this.server = server;
        this.factory = factory;
        this.systemFile = systemFile;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String result = "<html>" +
                "<head>\n" +
                "</head>"+
                "<body>"+
                "<h1>Folder contains : </h1>";

        String tmpUrl = exchange.getRequestURI().toString().trim();
        String tmp = tmpUrl.substring(0,tmpUrl.lastIndexOf('/'));
        result += (tmp.length() > 0)?"<a href=\"" + tmpUrl.substring(0,tmpUrl.lastIndexOf('/')) + "\">Dossier parent</a>":"";

        List<File> files = ((Folder) systemFile.get(exchange.getRequestURI().toString())).getFiles();

        BufferedReader httpInput = new BufferedReader(new InputStreamReader(
                exchange.getRequestBody(), "UTF-8"));
        StringBuilder in = new StringBuilder();
        String input;
        while ((input = httpInput.readLine()) != null) {
            in.append(input).append(" ");
        }
        httpInput.close();
        params = queryToMap(in.toString());

        /****************************************************/
        /*********** CRéATION D'UN DOSSIER ******************/
        /****************************************************/
        if (params.containsKey("createFolder")) {
            if(isExistInFolder(files, params.get("createFolder").trim())){
                result += "<p>Folder already Exist</p>";
            }else {
                Folder folder = factory.createFolder(params.get("createFolder").trim(), new ArrayList<>());
                if (folder == null) {
                    result += "<p>folderName invalid</p>";
                } else {
                    String currentPath = exchange.getRequestURI().toString().trim();
                    systemFile.put(currentPath + "/" + folder.getName(), folder);
                    ((Folder) systemFile.get(currentPath)).addFile(folder);
                    server.createContext(currentPath + "/" + folder.getName(), new FolderHandler(server, factory, systemFile));
                }
            }
        }

        /****************************************************/
        /************** CRéATION D'UN FILE ******************/
        /****************************************************/
        if (params.containsKey("importFile")) {
            String filePath = params.get("importFile");
            filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.name()).trim();
            java.io.File file = new java.io.File(filePath);

            byte[] content = Files.readAllBytes(Path.of(file.getAbsolutePath()));
            File regularFile = factory.createRegularFile(file.getName(), content.length, content);
            if (regularFile == null) {
                result += "<p>File name invalid</p>";
            } else{
                systemFile.put(exchange.getRequestURI().toString() + "/" + file.getName(), regularFile);
                ((Folder) systemFile.get(exchange.getRequestURI().toString())).addFile(regularFile);
                server.createContext(exchange.getRequestURI().toString() + "/" + file.getName(),
                        new RegularFileHandler(systemFile));
            }
        }

        /****************************************************/
        /************** CRéATION D'UN LINKFILE **************/
        /****************************************************/
        if (params.containsKey("createLink")) {
            String linkFilePath = params.get("createLink");
            linkFilePath = URLDecoder.decode(linkFilePath, StandardCharsets.UTF_8.name()).trim();

            String nameFile = linkFilePath.substring(linkFilePath.lastIndexOf('/') + 1);
            LinkFile link = factory.createLinkFile("link_" + nameFile,linkFilePath);

            systemFile.put(exchange.getRequestURI().toString() + "/link_" +nameFile , link);

            ((Folder) systemFile.get(exchange.getRequestURI().toString())).addFile(link);
            server.createContext(exchange.getRequestURI().toString() + "/link_" +nameFile,
                    new LinkFileHandler(systemFile));
        }

        for (File f : files) {
            result += "<li><a href=\"" + systemFile.get(exchange.getRequestURI().toString()).getName() + "/" +f.getName() + "\">" + f.getName() + "</a> size : " + f.getSize() + "</li>";
        }
        result  += "</ul>";

        result +="<h3>Operation filesystem :</h3>\n" +
                "<form action=\"\" method=\"POST\">\n" +
                "  <label for=\"importFile\">Import file :</label>\n" +
                "  <input id=\"importFile\" name=\"importFile\" required /><br /><br />\n" +
                "  <input type=\"submit\" value=\"Import file\">\n" +
                "</form>" +
                "<form action=\"\" method=\"POST\">\n" +
                "  <label for=\"createFolder\">Name of folder :</label>\n" +
                "  <input id=\"createFolder\" name=\"createFolder\" required /><br/><br/>\n" +
                "  <input type=\"submit\" value=\"Create Folder\"/>\n" +
                "</form>" +
                "<form action=\"\" method=\"POST\">\n" +
                "  <label for=\"createLink\">Path of link file :</label>\n" +
                "  <input id=\"createLink\" name=\"createLink\" required><br/><br/>\n" +
                "  <input type=\"submit\" value=\"Create Link\" />\n" +
                "</form>";
        result += "</body></html>";

        exchange.sendResponseHeaders(200, result.length());
        exchange.getResponseBody().write(result.getBytes());
        exchange.getResponseBody().close();
    }

    /**
     * Transform query url to Map<String,String>
     * @param query List of param in url
     * @return {@link Map<String,String>}
     */
    private Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Check if file already exists in folder
     * @param files List of file
     * @param newNameFile Name of new file
     * @return boolean
     */
    private boolean isExistInFolder(List<File> files, String newNameFile){
        for(File f : files){
            if(f.getName().equals(newNameFile))
                return true;
        }
        return false;
    }

}
