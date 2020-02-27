package me.supercode.view;

import me.supercode.exceptions.FileSendException;
import me.supercode.file.FileReader;
import me.supercode.web.MessageAccept;
import me.supercode.web.MessageAppend;
import me.supercode.web.ConnectionListener;
import me.supercode.web.Client;
import me.supercode.web.Operator;
import me.supercode.web.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JFrame implements MessageAccept, ConnectionListener {

    private JButton sendBtn;
    private JTextArea showText, enterText;
    private JPanel panel;
    private JList<String> receive;
    private MessageAppend append;
    private FileReader reader;
    private String targetAddr, filepath;
    private Operator op;
    private boolean serverMode;
    private boolean connected;
    private boolean isFile = false;
    private DefaultListModel<String> listContent;
    private JLabel status;
    private List<String> listCache;

    MainPanel(boolean serverMode) throws Exception {
        this(serverMode, null);
    }

    MainPanel(boolean serverMode, String address) throws Exception {
        this.targetAddr = address;
        this.serverMode = serverMode;
        reader = new FileReader();
        listCache = new ArrayList<>();
    }

    private void initConnection() {
        if (this.serverMode) {
            serverMode();
        } else {
            clientMode(this.targetAddr);
        }
    }

    private void serverMode() {
        op = new Server(this, this);
        setAppend(op);
        startOperator();
    }

    private void clientMode(String remote) {
        op = new Client(remote, this, this);
        setAppend(op);
        startOperator();
        this.connected = true;
    }

    private void startOperator() {
        op.start();
    }

    private void setAppend(MessageAppend append) {
        this.append = append;
    }

    @Override
    public void accept(String s) {
        appendText(s);
    }

    @Override
    public void onConnect(String addr) {
        this.targetAddr = addr;
        this.connected = true;
        appendText(addr + "已連接\n");
    }

    @Override
    public void onError(String msg, Throwable e) {
        e.printStackTrace();
        errorMessage(msg);
    }

    @Override
    public void onDisConnect() {
        System.exit(-1);
    }

    private void initViews() {
        setTitle("内網剪貼板");
        setBounds(1000,400,700,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        enterText = new JTextArea();
        sendBtn = new JButton();
        showText = new JTextArea();
        panel = new JPanel();
        receive = new JList<>();
        status = new JLabel();
        receive.setBorder(BorderFactory.createTitledBorder("接受列表"));
        receive.setBounds(0,0,120,600);
        receive.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listContent = new DefaultListModel<>();
        receive.setModel(listContent);
        enterText.setSize(380, 200);
        enterText.setRows(8);
        enterText.setLineWrap(true);
        showText.setLineWrap(true);
        showText.setBounds(120,0,380,400);
        showText.setBorder(BorderFactory.createTitledBorder("接受内容"));
        panel.setBounds(10,10,100,100);
        sendBtn.setText("發送");
        sendBtn.setSize(50,30);
        panel.add(enterText);
        panel.add(sendBtn);
        status.setSize(1000,20);
        panel.add(status);
        add(receive, BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);
        add(showText, BorderLayout.EAST);
    }

    private void initActions() {
        sendBtn.addActionListener(e->{
            if (!this.connected) {
                errorMessage("沒有客戶端連接");
                return;
            }
            if (!isFile) {
                String content = enterText.getText();
                appendText(content);
                reader.append(content+"\n");
                append.append(content);
            } else {
                try {
                    sendFile();
                    appendText("发送文件" + filepath);
                } catch (FileSendException ee) {
                    errorMessage("无法发送文件");
                }
            }
            enterText.setText("");
        });
        enterText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    isFile = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        enterText.setTransferHandler(new TransferHandler() {
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                if (t.getTransferDataFlavors()[0] == DataFlavor.javaFileListFlavor)
                    try {
                        Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                        String filepath = o.toString();
                        enterText.setText(filepath+"\n");
                        MainPanel.this.filepath = filepath;
                        isFile = true;
                    } catch (Exception e) {
                        errorMessage("无法发送文件:" + e.getLocalizedMessage());
                    }
                else
                    try {
                        Object o = t.getTransferData(DataFlavor.stringFlavor);
                        enterText.setText((String)o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                return true;
            }
            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (DataFlavor flavor : flavors) {
                    if (DataFlavor.javaFileListFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }
        });
        receive.addListSelectionListener(e -> {
            showText.setText(listCache.get(receive.getSelectedIndex()));
        });
    }

    private void sendFile() throws FileSendException {
        if (!filepath.startsWith("[")||!filepath.endsWith("]"))
            throw new FileSendException("文件格式不正确："+filepath);
        File file = new File(filepath.replace("[", "").replace("]",""));
        if (!file.exists())
            throw new FileSendException("文件不存在");
        else if (file.isDirectory())
            throw new FileSendException("无法发送文件夹");
        else if (!file.canRead())
            throw new FileSendException("无法读取文件");
        append.sendFile(file);
    }

    private void getHistory() {

    }
    
    private void appendText(String s) {
        listContent.addElement(s.substring(0,s.length()>30?30:s.length()-1));
        listCache.add(s);
    }

    private void errorMessage(String s) {
        status.setText(s);
        new Thread(()->{
            try {
                Thread.sleep(2000);
                status.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    void run() {
        initViews();
        initActions();
        setVisible(true);
        new Thread(this::initConnection).start();
    }
}
