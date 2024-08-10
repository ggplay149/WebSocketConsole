package com.WebSocketServer.Console;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class EchoServer {

    public void startServer(){

        ServerSocket server = null;
        Socket client = null;

        try{

            //ip, port번호 지정하여 소켓서버 생성
            InetAddress loopbackAddress = InetAddress.getByName("127.0.0.1");
            server = new ServerSocket(8109,50,loopbackAddress);
            while(true){

                System.out.println("\n\n::: Server > Waiting for request :::");

                // 다른 원격 호출 대기, request가 온다면 Socket 객체 리턴
                client = server.accept();

                System.out.println("::: Server > Accepted :::");

                //Socket으로 넘어온 데이터를 InputStream 으로 받기
                InputStream stream = client.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(stream));

                //넘어오는 데이터를 StringBuilder에 쌓기
                String data = null;
                StringBuilder receivedData = new StringBuilder();
                while((data = in.readLine())!= null){
                    receivedData.append(data);
                }

                //받은 데이터 출력 후 닫아주기
                System.out.println("::: ReceivedData >  " + receivedData + " :::");

                in.close();
                stream.close();
                client.close();

                //더이상 받을 자료가 없고 "EXIT" 전송될시 서버 닫기
                if(receivedData!=null && "EXIT".equals(receivedData.toString())){
                    System.out.println("::: Stop SocketServer :::");
                    break;
                }

                System.out.println("===================");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(server!=null){
                try{
                    server.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
