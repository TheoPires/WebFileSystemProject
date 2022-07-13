package fr.univrouen.file_system.app.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.factory.FileFactory;
import fr.univrouen.file_system.file.regular_file.RegularFile;

import java.io.IOException;
import java.util.Map;

/**
 * Implementation of HttpHandler to define application behavior
 * when you use a RegularFile
 */
public class RegularFileHandler implements HttpHandler {
    private Map<String, File> systemFile;

    public RegularFileHandler(Map<String, fr.univrouen.file_system.file.File> systemFile) {
        this.systemFile = systemFile;
    }

    public void handle(HttpExchange exchange) throws IOException {
        Headers h = exchange.getResponseHeaders();
        RegularFile file = (RegularFile) systemFile.get(exchange.getRequestURI().toString());
        String f = new String(file.getContent());
        h.add("Content-Type", "application/octet-stream");
        exchange.sendResponseHeaders(200, f.length());
        exchange.getResponseBody().write(f.getBytes());
        exchange.getResponseBody().close();
    }
}
