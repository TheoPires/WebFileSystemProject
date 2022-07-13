package fr.univrouen.file_system.app.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.link.LinkFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Implementation of HttpHandler to define application behavior
 * when you use a LinkFile
 */
public class LinkFileHandler implements HttpHandler {

    private Map<String, File> systemFile;

    public LinkFileHandler(Map<String, fr.univrouen.file_system.file.File> systemFile) {
        this.systemFile = systemFile;
    }

    public void handle(HttpExchange exchange) throws IOException {
        LinkFile file = (LinkFile) systemFile.get(exchange.getRequestURI().toString());
        if (!systemFile.containsKey(file.getPath())){
            String response = "<html>" +
                    "<body>"+
                    "No source at this context";
            String tmpUrl = exchange.getRequestURI().toString().trim();
            String tmp = tmpUrl.substring(0,tmpUrl.lastIndexOf('/'));
            response += (tmp.length() > 0)?"<a href=\"" + tmpUrl.substring(0,tmpUrl.lastIndexOf('/')) + "\">Dossier parent</a>":"";

            response += "</body></html>";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        } else {
            Headers h = exchange.getResponseHeaders();
            h.add("Location", file.getPath());
            exchange.sendResponseHeaders(302, 0);
            exchange.getResponseBody().close();
        }
    }
}
