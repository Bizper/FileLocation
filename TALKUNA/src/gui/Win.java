package gui;

import core.Chatting;
import core.DateUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Win extends Frame {

    private static Win win;

    private TextArea messages = new TextArea("", 760, 460, TextArea.SCROLLBARS_VERTICAL_ONLY);

    private TextField input = new TextField();

    private Label nick = new Label("NICK:");

    private TextField nick_input = new TextField();

    private Button send = new Button("SEND");

    private Button list = new Button("在线列表");

    private Button count = new Button("在线人数");

    private Chatting chatting;

    private keySolution key = new keySolution();

    public Win() {
        win = this;
        this.setTitle("TALKUNA");
        this.setResizable(false);
        this.setBounds(200, 200, 800, 600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatting.close();
                System.exit(0);
            }
        });
        this.setLayout(null);
        this.initComponent();
        this.add(messages);
        this.add(input);
        this.add(send);
        this.add(nick_input);
        this.add(nick);
        this.add(list);
        this.add(count);
        this.setVisible(true);
        this.addKeyListener(key);
        this.chatting = new Chatting();
    }

    private void initComponent() {
        messages.setBounds(20, 47, 760, 460);
        messages.setEditable(false);
        input.setBounds(20, 560, 680, 20);
        nick.setBounds(20, 520, 40, 20);
        nick_input.setBounds(70, 520, 400, 20);
        list.setBounds(490, 520, 80, 20);
        count.setBounds(590, 520, 80, 20);
        count.addActionListener(e -> chatting.send("LUNA:LiveCount"));
        list.addActionListener((e) -> chatting.send("LUNA:LiveList"));
        send.setBounds(720, 560, 60, 20);
        send.addActionListener((e) -> {
            if(!input.getText().isEmpty() && !nick_input.getText().isEmpty()) {
                String nick = nick_input.getText();
                String message = input.getText();
                chatting.send(nick + "-" + message);
                append(nick + " " + DateUtil.getDate() + ": " + message);
                input.setText("");
            }
            else append("WARNING: 输入不能为空！");
        });
        input.addKeyListener(key);
    }

    public static void append(String s) {
        win._append(s);
    }

    private void _append(String s) {
        messages.append(s + "\n");
    }

    public static void main(String args[]) {
        new Win();
    }

    private class keySolution extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    if(!input.getText().isEmpty() && !nick_input.getText().isEmpty()) {
                        String nick = nick_input.getText();
                        String message = input.getText();
                        chatting.send(nick + "-" + message);
                        append(nick + " " + DateUtil.getDate() + ": " + message);
                        input.setText("");
                    }
                    else append("WARNING: 输入不能为空！");
                    break;
            }
        }
    }

}
