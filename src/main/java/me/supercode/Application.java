package me.supercode;

import me.supercode.view.IndexPanel;

public class Application {
    public static void main(String[] args) {
        try {
            IndexPanel panel = new IndexPanel();
            panel.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
