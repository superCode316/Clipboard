package me.supercode.web;

import java.net.Socket;

public class Client extends Operator implements MessageAppend {

    public Client(String remoteAddr, MessageAccept accept, ConnectionListener listener) {
        setAccept(accept);
        setListener(listener);
        try {
            setSocket(new Socket(remoteAddr, 316));
        } catch (Exception e) {
            getListener().onError(e.getLocalizedMessage(), e);
        }
    }

}
