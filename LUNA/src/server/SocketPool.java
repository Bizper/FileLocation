package server;

import java.util.ArrayList;

public class SocketPool {

    private static ArrayList<SocketThread> pool = new ArrayList<>();

    static int put(SocketThread s) {
        pool.add(s);
        return pool.indexOf(s);
    }

    public static SocketThread get(int index) {
        return pool.get(index);
    }

    static void push(String str, SocketThread sender) {
        for(SocketThread st : pool) {
            if(st != sender) st.send(str);
        }
    }

    static void push(String str) {
        for(SocketThread st : pool) {
            if(st != null) st.send(str);
        }
    }

    static void remove(SocketThread st) {
        pool.remove(st);
    }

    static int size() {
        return pool.size();
    }

    static String list() {
        StringBuilder stringBUilder = new StringBuilder();
        for(SocketThread st : pool) {
            stringBUilder.append(st.getID()).append(" ").append(st.getNick()).append("\n");
        }
        stringBUilder.deleteCharAt(stringBUilder.length() - 1);
        return stringBUilder.toString();
    }

}
