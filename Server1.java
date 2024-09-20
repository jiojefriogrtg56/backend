package task4.exercise2;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/data", new DataHandler());
        server.setExecutor(null);
        server.start();
    }

    static class DataHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try (InputStream requestBody = exchange.getRequestBody();
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = requestBody.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                String text = output.toString();

                Files.write(Paths.get("data.txt"), text.getBytes());

                exchange.sendResponseHeaders(200, -1);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.close();
            }
        }
    }
}