package server;

import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {

    private int id;
    private String nick = "";
    private Socket socket;
    private Server server;
    private BufferedReader bis;
    private PrintWriter bos;
    private boolean flag = false;

    public int getID() {
        return id;
    }

    public String getNick() {
        return nick == null || nick.isEmpty() ? "这位用户还没有设置昵称" : nick;
    }

    public SocketThread(int id, Socket s, Server server) {
        this.id = id;
        this.socket = s;
        this.server = server;
        try {
            bis = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
            bos = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = true;
        this.start();
    }

    @Override
    public void run() {
        while(flag) {
            try {
                if(socket.isClosed() || !socket.isConnected()) {
                    this.join();
                    break;
                }
                StringBuilder str = new StringBuilder();
                char c;
                int i;
                while((i = bis.read()) != -1) {
                    c = (char) i;
                    str.append(c);
                    if(str.toString().contains("eof")) {
                        String s = str.toString().substring(0, str.length() - 3);//去除eof结束符
                        codeChecker(s);
                        str.delete(0, str.length());//清空缓存
                    }
                }
                System.out.println("message from " + socket.getInetAddress() + ": " + str);
            } catch (Exception e) {
                System.exit(0);
            }
        }
    }

    private void codeChecker(String str) {
        try {
            if(str.startsWith("LUNA") && str.contains(":")) {
                String code = str.split("\\:")[1];
                switch(code) {
                    case "ForcedExit"://如果是客户端的退出信息
                        SocketPool.push("--------" + (nick == null || nick.isEmpty() ? "客户端ID：" + id : nick) + "离开了聊天室--------", this);
                        System.out.println("client: " + id + " exit the chatting room.");
                        server.removeID(id);
                        socket.close();
                        SocketPool.remove(this);
                        flag = false;
                        this.join();
                        break;
                    case "LiveCount"://客户端的清单信息
                        send("当前在线人数：" + SocketPool.size());
                        break;
                    case "LiveList":
                        send(SocketPool.list());
                        break;
                }
            } else {
                String nick = str.split("\\-")[0];//取得昵称
                if(!this.nick.equals(nick)) {
                    this.nick = nick;
                }
                String message = str.split("\\-")[1];//取得发送的消息内容
                System.out.println("message from " + id + ": " + this.nick + ":" + message);//控制台打印信息
                SocketPool.push(this.nick + "(" + id + ") " + DateUtil.getDate() + ": " + message, this);//向其它客户端推送信息
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    SocketThread send(String str) {
        str += "eof";
        bos.write(str);
        bos.flush();
        return this;
    }

}
