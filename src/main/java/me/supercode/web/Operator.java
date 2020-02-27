package me.supercode.web;

import java.io.File;
import java.net.Socket;

public abstract class Operator implements MessageAppend {

    private Socket socket;
    private InThread inThread;
    private OutThread outThread;
    private MessageAccept accept;
    private ConnectionListener listener;

    public void start(){
        try {
            inThread = new InThread(socket.getInputStream(), accept);
            outThread = new OutThread(socket.getOutputStream(), listener);
            inThread.start();
            outThread.start();
            listener.onConnect(getSocket().getRemoteSocketAddress().toString());
        } catch (Exception e) {
            listener.onError(e.getLocalizedMessage(), e);
        }
    }

    void setAccept(MessageAccept accept) {
        this.accept = accept;
    }

    void setSocket(Socket socket) {
        this.socket = socket;
    }

    private Socket getSocket() {
        return socket;
    }

    ConnectionListener getListener() {
        return listener;
    }

    void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    public boolean append(String m) {
        if (this.outThread != null) {
            this.outThread.append(m);
            return true;
        }
        return false;
    }

    public void sendFile(File f) {
        this.outThread.append(f);
    }
}
