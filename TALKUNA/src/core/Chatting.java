package core;

import gui.Win;

import java.io.*;
import java.net.Socket;

public class Chatting extends Thread {

    private Socket s;
    private PrintWriter bos;
    private BufferedReader bis;

    public Chatting() {
        try {
            s = new Socket("120.78.145.94", 9090);
            bos = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"), true);
            bis = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String str) {
        str += "eof";
        bos.write(str);
        bos.flush();
    }

    public void close() {
        send("LUNA:ForcedExit");
    }

    public void run() {
        int i;
        while(true) {
            try {
                while((i = bis.read()) != -1) {
                    receive(i);//接受消息
                }
            } catch (IOException e) {
                System.exit(0);
            }
        }
    }

    StringBuilder stringBuilder = new StringBuilder();

    public void receive(int i) {
        stringBuilder.append((char)i);
        if(stringBuilder.toString().contains("eof")) {
            String str = stringBuilder.toString().substring(0, stringBuilder.length() - 3);
            Win.append(str);
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

}
