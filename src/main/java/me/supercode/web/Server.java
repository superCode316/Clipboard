package me.supercode.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server extends Operator implements MessageAppend {

    public Server(MessageAccept accept, ConnectionListener listener) {
        setAccept(accept);
        setListener(listener);
        System.out.println("網絡監聽端口316");
        try {
            accept.accept("本機地址：" + getLocalAddress());
        } catch (Exception e) {
            listener.onError(e.getLocalizedMessage(), e);
        }
        try {
            ServerSocket serverSocket = new ServerSocket(316);
            setSocket(serverSocket.accept());
        } catch (IOException e) {
            getListener().onError(e.getLocalizedMessage(), e);
        }
    }

    private String getLocalAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
