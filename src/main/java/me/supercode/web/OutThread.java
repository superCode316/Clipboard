package me.supercode.web;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class OutThread extends Thread {

    private DataOutputStream out;
    private Queue<String> queue;
    private Queue<File> fileQueue;
    private ConnectionListener listener;

    OutThread(OutputStream out, ConnectionListener listener) {
        this.out = new DataOutputStream(out);
        this.listener = listener;
        queue = new ArrayDeque<>();
        fileQueue = new ArrayDeque<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!queue.isEmpty()) {
                    while (!queue.isEmpty()) {
                        String s = queue.remove();
                        out.writeUTF(s);
                    }
                }
                if (!fileQueue.isEmpty()) {
                    File f = fileQueue.remove();
                    out.writeUTF("&me.supercode.file");
                    out.writeUTF("&filename="+f.getName());
                    FileInputStream fin = new FileInputStream(f);
                    byte[] bytes = new byte[2048];
                    int len;
                    while((len=fin.read(bytes))!=-1)
                        out.write(bytes, 0, len);
                    out.flush();
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                listener.onError(e.getLocalizedMessage(), e);
            }
        }
    }

    void append(String s) {
        if (this.queue != null) this.queue.add(s);
    }

    void append(File f) {
        this.fileQueue.add(f);
    }
}
