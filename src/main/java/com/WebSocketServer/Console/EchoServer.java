package com.WebSocketServer.Console;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
public class EchoServer {

    public void startServer() {

        ServerSocket server = null;
        Socket client = null;

        HttpMethodHandler httpMethodHandler = new HttpMethodHandler();

        try {

            //ip, port번호 지정하여 소켓서버 생성
            InetAddress loopbackAddress = InetAddress.getByName("127.0.0.1");
            server = new ServerSocket(8109, 50, loopbackAddress);
            while (true) {

                System.out.println("\n::: Server > Waiting for request :::\n");

                // 다른 원격 호출 대기, request가 온다면 Socket 객체 리턴
                client = server.accept();

                //Socket으로 넘어온 데이터를 InputStream 으로 받기
                InputStream stream = client.getInputStream();


                //Head에서 Method 값 출력
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                String method = reader.readLine().split(" ")[0];
                System.out.println("Method : " + method);


                //method별 Message 값 가져오기
                String message = httpMethodHandler.getMessage(method,reader);
                System.out.println("Message : " + message);


                System.out.println("\n===================");

                // 클라이언트에 응답 보내기
                OutputStream output = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                writer.write("HTTP/1.1 200 OK\r\n");
                writer.write("Content-Type: text/plain\r\n");
                writer.write("Content-Length: " + message.length() + "\r\n");
                writer.write("\r\n");
                writer.write("SUCCESS");

                writer.flush();
                writer.close();
                stream.close();
                client.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
