package com.jim.sprjfx.service;

import com.jim.sprjfx.handler.Serverhandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RestController
public class SocketThread {

    private int port = 2221;
    public boolean a = true;
    public boolean firstTime = true;

    ServerSocket server = new ServerSocket(port);

    public SocketThread() throws IOException {
    }

    public String turnOFFServer() throws IOException {
        this.server.close();
        return "Success";
    }

    public String turnONServer() throws IOException {
        System.out.println(server.isClosed());
        if(server.isClosed()) {
            this.server = new ServerSocket(port);
        }
        startSocket();
        return "Success";

    }

    //    private boolean serverFlag = true;
    public void test() throws IOException {
        this.a = !a;
    }

    public void NNNew() throws IOException {
        this.server = new ServerSocket(port);
    }

    @Async("socketthreadPool")
    public void startSocket() throws IOException {

        if(this.server.isClosed()){
            this.server = new ServerSocket(port);
        }

        if(!this.server.isClosed()){

            log.info("Socket 服务器准备就绪");
            log.info("服务端信息："+server.getInetAddress()+"\tP:"+server.getLocalPort());

            while(true){
                // 得到客户端
                Socket client = server.accept();
                // 客户端异步线程
                SocketThread.ClientHandler clientHandler = new SocketThread.ClientHandler(client);
                // 线程启动
                clientHandler.start();
            }
        }else{
            this.server.close();
            log.info("服务器成功关闭Success");
            return;
        }
    }


    public static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag = true;
        ClientHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            super.run();

            log.info("新的客户端连接：\t"+socket.getInetAddress()+"\tP:\t"+socket.getPort()+"\t本机IP：\t"+socket.getLocalAddress()+"\tP:\t"+socket.getLocalPort());

            try{
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                System.out.println("");
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("");

                while(flag){
                    String str = socketInput.readLine();
                    try {
                        JSONObject json = new JSONObject(str);
                        log.info("回送：\t"+str.length()+"\tJSONObject:\t"+json);
                    }catch (Exception e){
                        log.info("json解析错误");
                    }


                    if("bye".equalsIgnoreCase(str)){
                        flag = false;
                        socketOutput.println("bye");
                    }else{
                        log.info("回送：\t"+str.length()+"\tMessage:\t"+str);
                    }
                }

                socketInput.close();
                socketOutput.close();
            }catch (Exception e){
                log.error("Error link");
//                log.error(String.valueOf(e));
            }finally {
                // 连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("客户端已关闭\t"+socket.getInetAddress());
            }
        }

    }


}
