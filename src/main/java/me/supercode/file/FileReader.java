package me.supercode.file;

import me.supercode.exceptions.FileCannotOpenException;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReader implements FileOperate{

    private static String USERHOME = System.getProperty("user.home");
    private static String defaultSaveFileName = "webpasteboard.his";

    private File logFile;

    public FileReader() throws FileCannotOpenException {
        logFile = new File(defaultSaveFileName);
        if (logFile.isDirectory())
            throw new FileCannotOpenException("文件無法打開");
        if (!logFile.exists())
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                throw new FileCannotOpenException("文件無法建立");
            }
    }

    public ImageIcon getIconImage() {
        return new ImageIcon(USERHOME + "1.jpg");
    }

    @Override
    public List<String> readFile() throws FileCannotOpenException {
        List<String> content= new ArrayList<>();
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(logFile);
            byte[] bytes = new byte[1024];
            while(fin.read(bytes)!=-1) {
                content.add(new String(bytes));
            }
        } catch (FileNotFoundException e) {
            throw new FileCannotOpenException("文件不存在");
        } catch (IOException e) {
            throw new FileCannotOpenException("文件無法讀取");
        } finally {
            try {
                if (fin !=null) fin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public void append(String line) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile);
            fos.write(line.getBytes());
        } catch (IOException e) {
            System.out.println("無法添加至文件");
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
