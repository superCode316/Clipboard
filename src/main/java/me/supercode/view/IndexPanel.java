package me.supercode.view;

import javax.swing.*;
import java.util.regex.Pattern;

public class IndexPanel extends JFrame {

    private JPanel main;
    private JLabel title, address;
    private JTextArea addressText;
    private JRadioButton server, client;
    private ButtonGroup mode;
    private JButton submitBtn;
    private boolean serverMode = false;
    private final String addressPattern = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";

    public IndexPanel() {
        setBounds(400,400,300,200);
        setLayout(null);
        initViews();
        initActions();
        add(main);
    }

    private void initActions() {
        submitBtn.addActionListener(e -> {
            setVisible(false);
            String addr = addressText.getText();
//            if (checkAddress(addr))
            try {
                MainPanel main = serverMode ? new MainPanel(serverMode) : new MainPanel(serverMode, addr);
                main.run();
            } catch (Exception ee) {
                System.out.println(ee.getMessage());
                System.exit(-1);
            }
        });
        server.addActionListener(e -> serverMode = true);
        client.addActionListener(e -> serverMode = false);
    }

    private void initViews() {
        main = new JPanel();
        main.setBounds(10,10,280,180);
        title = new JLabel();
        server = new JRadioButton("server"); client = new JRadioButton("client", true);
        mode = new ButtonGroup();
        submitBtn = new JButton();
        address = new JLabel();
        addressText = new JTextArea();


        setTitle("選擇模式");
        title.setText("請選擇模式");
        submitBtn.setText("確認");
        address.setText("地址：");
        addressText.setColumns(20);

        mode.add(server);
        mode.add(client);
        main.add(title);
        main.add(client);
        main.add(server);
        main.add(address);
        main.add(addressText);
        main.add(submitBtn);
    }

    public void run() {
        setVisible(true);
    }

    private boolean checkAddress(String a) {
        return Pattern.matches(addressPattern, a);
    }
}
