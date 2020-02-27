package me.supercode.web;

import java.io.File;

public interface MessageAppend {
    boolean append(String m);
    void sendFile(File f);
}
