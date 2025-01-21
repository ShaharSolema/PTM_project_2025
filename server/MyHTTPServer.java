package test.server;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;
import java.net.ServerSocket;

import test.servlets.Servlet;

public class MyHTTPServer extends Thread implements HTTPServer {

    private ConcurrentHashMap<String, Servlet> getHandlers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Servlet> postHandlers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Servlet> deleteHandlers = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;
    private volatile boolean isServerStopped = false;//volatile-can change in every moment
    private ExecutorService handlerPool;
    private final int threadCount;
    private final int port;


    public MyHTTPServer(int port, int threadCount) {//constructor
        handlerPool = Executors.newFixedThreadPool(threadCount);
        this.port = port;
        this.threadCount = threadCount;
    }

    public void addServlet(String method, String path, Servlet servlet) {//adding servlet to the map
        if (path == null || servlet == null) {
            return;
        }
        method = method.toUpperCase();

        switch (method) {//depend on kind of the request
            case "GET":
                getHandlers.put(path, servlet);
                break;
            case "POST":
                postHandlers.put(path, servlet);
                break;
            case "DELETE":
                deleteHandlers.put(path, servlet);
                break;
        }
    }

    public void removeServlet(String method, String path) {//removing servlet from map
        if (path == null) {
            return;
        }
        method = method.toUpperCase();

        switch (method) {
            case "GET":
                getHandlers.remove(path);
                break;
            case "POST":
                postHandlers.remove(path);
                break;
            case "DELETE":
                deleteHandlers.remove(path);
                break;
        }
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) { // listening to the given port
            this.serverSocket = serverSocket;
            serverSocket.setSoTimeout(1000); // set timout for 1 sec.

            while (!isServerStopped) { // Keep the server running until stopped
                try {
                    // accept a client connection
                    Socket clientSocket = serverSocket.accept();

                    // submit a task to the handler pool to handle the client request
                    handlerPool.submit(() -> {
                        try {
                            Thread.sleep(125); // sleep to ensure proper request reception
                            BufferedReader reader = createBufferedReader(clientSocket); // create a reader for the client socket

                            // Parse the HTTP request
                            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(reader);
                            ConcurrentHashMap<String, Servlet> handlers; // declare a variable for storing the correct handler map

                            if (requestInfo != null) {
                                switch (requestInfo.getHttpCommand()) { // checking the command
                                    case "GET":
                                        handlers = getHandlers;
                                        break;
                                    case "POST":
                                        handlers = postHandlers;
                                        break;
                                    case "DELETE":
                                        handlers = deleteHandlers;
                                        break;
                                    default:
                                        throw new IllegalArgumentException("Unsupported HTTP command: " + requestInfo.getHttpCommand());
                                }

                                // find the best matching handler based on the URI
                                String bestMatchPath = "";
                                Servlet matchingServlet = null;
                                for (Map.Entry<String, Servlet> entry : handlers.entrySet()) {
                                    if (requestInfo.getUri().startsWith(entry.getKey()) && entry.getKey().length() > bestMatchPath.length()) {
                                        bestMatchPath = entry.getKey(); // update the URI
                                        matchingServlet = entry.getValue(); // update the servlet
                                    }
                                }

                                // if a matching found,using handle method the request
                                if (matchingServlet != null) {
                                    matchingServlet.handle(requestInfo, clientSocket.getOutputStream());
                                }
                            }
                            reader.close(); // closing
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace(); // handle exceptions
                        } finally {
                            try {
                                clientSocket.close(); // Close the client socket after handling the request
                            } catch (IOException e) {
                                e.printStackTrace(); // Handle any errors while closing the socket
                            }
                        }
                    });
                } catch (IOException e) {
                    if (isServerStopped) {
                        break; // exit the loop if the server stopped
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // server cannot open
        }
    }


    // method creates a BufferedReader from the input stream of the client socket
    private static BufferedReader createBufferedReader(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream(); // input stream from the client socket
        int availableBytes = inputStream.available(); // number of bytes available in the input stream
        byte[] buffer = new byte[availableBytes]; // buffer to hold the available bytes
        int bytesRead = inputStream.read(buffer, 0, availableBytes); // bytes into the buffer

        // returns a BufferedReader,reads from the byte array input stream
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(buffer, 0, bytesRead) // creat new ByteArrayInput stream.
                )
        );
    }

    // This method starts the server by setting the flag to false and calling the super start method
    public void start() {
        isServerStopped = false; // Ensure that the server is not stopped
        super.start(); // Call the start method of the superclass to begin server operation
    }

    // This method stops the server and shuts down the thread pool
    public void close() {
        isServerStopped = true; // Set the flag to indicate the server has been stopped
        handlerPool.shutdownNow(); // Immediately shut down all threads in the handler pool
    }

    // returns the thread pool object
    public Object getThreadPool() {
        return handlerPool;
    }

}
