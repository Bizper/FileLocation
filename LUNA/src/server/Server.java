package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {

    private ServerSocket ss;
    private Random random;
    private ArrayList<Integer> id_lists;
    private int ID_MAX;

    {
        try {
            ss = new ServerSocket(9090);
            random = new Random();
            id_lists = new ArrayList<>();
            ID_MAX = Integer.MAX_VALUE;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() {
        while(true) {
            try {
                int id = random.nextInt(ID_MAX);
                while(id_lists.contains(id)) {
                    id = random.nextInt(ID_MAX);
                }
                id_lists.add(id);
                Socket s = ss.accept();
                System.out.println("new Socket connected! from ip:" + s.getInetAddress() + " as id:" + id);
                SocketPool.push("--------欢迎客户端(id:" + id + ")进入聊天室--------");
                SocketPool.put(new SocketThread(id, s, this).send("--------" + id + "号客户端，欢迎加入聊天室--------"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeID(int id) {
        id_lists.remove(Integer.valueOf(id));
    }

    public static void main(String argsp[]) {
        new Server();
    }

}
