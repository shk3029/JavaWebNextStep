package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            String line = bufferedReader.readLine();
            log.debug("request line : {}", line);
            if(line == null) return;
            String url = HttpRequestUtils.getUrl(line);

            byte[] body = null;

            Map<String, String> headerMap = new HashMap<>();

            while(!line.equals("")) {
                log.debug("header : {} ", line );
                line = bufferedReader.readLine();
                String[] headerTokens = line.split(":");
                if(headerTokens.length == 2) {
                    headerMap.put(headerTokens[0], headerTokens[1].trim());
                }
            }
        /*
            // Get 방식
            if(url.startsWith("/user/create?")) {
                int index = url.indexOf("?");
                String requestUrl =  url.substring(0, index);
                String paramUrl =  url.substring(index+1);
                Map<String, String> map = HttpRequestUtils.parseQueryString(paramUrl);
                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
                log.debug("requestUrl : {}", requestUrl);
                log.debug("paramUrl : {}", paramUrl);
                log.debug("User Info : {}", user);
                url = "/index.html";
            }
        */

            // Post 방식
            if(url.startsWith("/user/create")) {
                String postRequestBody = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
                log.debug("readData : {}", postRequestBody);
                Map<String, String> map = HttpRequestUtils.parseQueryString(postRequestBody);
                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
                log.debug("user : {}", user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

            } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
