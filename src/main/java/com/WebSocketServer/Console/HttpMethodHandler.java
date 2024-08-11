package com.WebSocketServer.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
public class HttpMethodHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String getMessage(String method, String path, BufferedReader reader) throws IOException {

        String message = null;
        String line;

        switch (method){
            case "GET":
                message = path.split("=")[1];
                break;

            case "POST":
            case "PUT":

                // Content-Length 찾기
                int contentLength = 0;
                while (!(line = reader.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                // Body 읽기
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                String body = new String(bodyChars);

                PostOrPutRequest postOrPutRequest = objectMapper.readValue(body.toString(), PostOrPutRequest.class);
                message = postOrPutRequest.message();

                break;
        }
        return message;
    }
}
