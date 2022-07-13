package fr.univrouen.file_system.app;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.factory.FileFactory;
import fr.univrouen.file_system.file.factory.StdFileFactory;
import fr.univrouen.file_system.file.folder.Folder;
import fr.univrouen.file_system.app.handler.FolderHandler;
import fr.univrouen.file_system.app.handler.RegularFileHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<String, String> params = new HashMap<>();
    private Map<String, fr.univrouen.file_system.file.File> systemFile = new HashMap<>();

    /**
     * Constructor for a Server with empty "root" folder
     * @param address Address of server (Exemple: "127.0.0.1"
     * @param port Port of server (Exemple : 80 )
     * @throws IOException
     */
    public Server(String address, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(address, port), 5);
        FileFactory factory = new StdFileFactory();

        final Folder root = factory.createFolder("root", new ArrayList<File>());
        systemFile.put("/"+root.getName(), root);
        server.createContext("/"+root.getName(), new FolderHandler(server, factory,systemFile));
        server.createContext("/", new Inithandler("/"+root.getName()));
        server.setExecutor(null);
        server.start();
        System.out.println("Listening on " + server.getAddress());
    }

    /**
     * Constructor for a Server with empty "root" folder
     * @param path Path of init folder
     * @param address Address of server (Exemple: "127.0.0.1"
     * @param port Port of server (Exemple : 80 )
     * @throws IOException
     */
    public Server(String path, String address, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(address, port), 5);
        FileFactory factory = new StdFileFactory();
        java.io.File file = new java.io.File(path);
        initFolder(server,"", file, factory);
        server.createContext("/", new Inithandler("/"+file.getName()));
        server.setExecutor(null);
        server.start();
        System.out.println("Listening on " + server.getAddress());
    }

    private fr.univrouen.file_system.file.File
    initFolder(HttpServer server, String currentPath, java.io.File folder, FileFactory factory) throws IOException {
        if (folder.isDirectory()) {
            Folder subFolder = factory.createFolder(folder.getName(), null);
            java.io.File[] list = folder.listFiles();
            if (list != null){
                for ( int i = 0; i < list.length; i++) {
                    //Recursive method to add File in subfolder
                    subFolder.addFile(initFolder(server,currentPath + "/" + subFolder.getName(),list[i],factory));
                }
                server.createContext( currentPath+ "/"+subFolder.getName(), new FolderHandler(server, factory, systemFile));
                systemFile.put(currentPath + "/" +subFolder.getName(), subFolder);
                return subFolder;
            } else {
                System.err.println(folder + " : Erreur de lecture.");
            }
        }else {
            //java.io.File to byte array
            FileInputStream fl = new FileInputStream(folder);
            byte[] fileData = new byte[(int) folder.length()];
            int size = fl.read(fileData);
            fl.close();
            //Return new RegularFile with java.io.File data
            File f = factory.createRegularFile(folder.getName(),size,fileData);
            server.createContext( currentPath+ "/"+f.getName(), new RegularFileHandler(systemFile));
            systemFile.put(currentPath + "/" +f.getName(), f);
            return f;
        }
        return null;
    }

    private class Inithandler implements HttpHandler {
        private String rootPath;

        Inithandler(String rootPath){
            this.rootPath = rootPath;
        }
        public void handle(HttpExchange exchange) throws IOException {
            Headers h = exchange.getResponseHeaders();
            h.add("Location", rootPath);
            exchange.sendResponseHeaders(302, 0);
            exchange.getResponseBody().close();
        }
    }
}
