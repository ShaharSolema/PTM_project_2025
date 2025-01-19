package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
		// implement
        try {
            String line=reader.readLine();
            if(line==null||line.isEmpty()){
                throw new IOException("Empty line");
            }
            String[] uriuri= line.split(" ");
            if(uriuri.length!=3){
                throw new IOException("Invalid URI");
            }
            String command=uriuri[0];
            Map<String, String> parameterss=new HashMap<>();
            String path=uriuri[1];
            String []uriParats=path.split("\\?");
            String[]urisegment={uriParats[0],uriParats[1]};
            String[]parameters=uriParats[2].split("&");
            for(String parameter:parameters){
                String[] key=parameter.split("=");
                if(key.length!=2){
                    throw new IOException("Invalid parameter");
                }
                parameterss.put(key[0],key[1]);
            }
            byte[] contents;
            while((line=reader.readLine())!=null&&!line.startsWith("Content")) {}
            StringBuilder builder=new StringBuilder();
            while((line=reader.readLine())!=null) {
                if(!line.isEmpty()) {
                    builder.append(line).append("\n");
                }
            }
                contents=builder.toString().getBytes();
                return (new RequestInfo(command,path,urisegment,parameterss,contents));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	// RequestInfo given internal class
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
