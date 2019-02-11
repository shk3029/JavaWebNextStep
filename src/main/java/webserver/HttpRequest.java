package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private Map<String, String> headerMap = new HashMap<>();
    private Map<String, String> paramMap = new HashMap<>();
    private RequestLine requestLine;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String line = bufferedReader.readLine();
            log.debug("request line : {}", line);
            if(line == null) return;;

            requestLine = new RequestLine(line);

            line = bufferedReader.readLine();
            while (!line.equals("")) {
                log.debug("header : {} ", line);
                String[] headerTokens = line.split(":");
                if(headerTokens.length == 2) {
                    headerMap.put(headerTokens[0].trim(), headerTokens[1].trim());
                }
                line = bufferedReader.readLine();
            }
            if(getMethod().isPost()) {
                String body  = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
                paramMap = HttpRequestUtils.parseQueryString(body);
            } else {
                paramMap = requestLine.getParamMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return headerMap.get(name);
    }

    public String getParam(String name) {
        return paramMap.get(name);
    }

}
















