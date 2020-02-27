package me.supercode.file;

import me.supercode.exceptions.FileCannotAppendException;
import me.supercode.exceptions.FileCannotOpenException;

import java.util.List;

public interface FileOperate {
    List<String> readFile() throws FileCannotOpenException;
    void append(String line) throws FileCannotAppendException;
}
