package test.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
            throw new IOException("Empty request line");
        }

        String[] requestParts = line.split(" ");
        String command = requestParts[0];
        String path = requestParts[1];

        String[] uriParts = path.split("\\?", 2);
        String[] uriSegments = uriParts[0].startsWith("/") ? uriParts[0].substring(1).split("/") : uriParts[0].split("/");
        Map<String, String> parameters = new HashMap<>();

        // Parse query parameters if present
        if (uriParts.length > 1) {
            String[] paramPairs = uriParts[1].split("&");
            for (String param : paramPairs) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    parameters.put(keyValue[0], "");
                }
            }
        }

        // Skip the empty line between headers and content
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
        }

        //get the names to the map
        line = reader.readLine();
        if (line != null && !line.isEmpty()) {
            String[] name = line.split("=");
            if (name.length == 2) {
                parameters.put(name[0], name[1]);
            } else {
                parameters.put(name[0], "");
            }
        }

        // teadt the content, if there any
        StringBuilder contentBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                contentBuilder.append(line).append("\n");
            }
        }

        byte[] content = contentBuilder.toString().getBytes();

        return new RequestInfo(command, path, uriSegments, parameters, content);
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
