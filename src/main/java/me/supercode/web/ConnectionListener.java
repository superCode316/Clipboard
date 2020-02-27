package me.supercode.web;

public interface ConnectionListener {
    void onConnect(String addr);
    void onError(String msg, Throwable e);
}
