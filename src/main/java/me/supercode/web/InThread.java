package me.supercode.web;

import java.io.*;

public class InThread extends Thread {
    private DataInputStream in;
    private MessageAccept accept;
    InThread(InputStream in, MessageAccept accept) {
        this.in = new DataInputStream(in);
        this.accept = accept;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String s = in.readUTF();
                if (!s.equals("&me.supercode.file"))
                    accept.accept(s);
                else {
                    String filename = in.readUTF().split("=")[1];
                    File f = new File(filename);
                    if (f.exists())
                        f.delete();
                    f.createNewFile();
                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] bytes = new byte[2048];
                    int l;
                    do {
                        l=in.read(bytes);
                        fos.write(bytes, 0, l);
                    } while (l==2048);
                    accept.accept("收到文件：" + filename);
                }
                Thread.sleep(2000);
            } catch (IOException e) {
                accept.onDisConnect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
