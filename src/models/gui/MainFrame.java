package models.gui;

import models.Message;
import models.chatClients.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private ChatClient chatClient;

    JTextArea txtChatArea = new JTextArea();
    JTextField txtInputMessage;
    public MainFrame(int width, int height, ChatClient chatClient){
        super("PRO 2022 ChatClient skC");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.chatClient = chatClient;

        initGui();
        setVisible(true);
    }

    private  void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        panelMain.add(initLoginPanel(), BorderLayout.NORTH);
        panelMain.add(initChatPanel(), BorderLayout.CENTER);
        panelMain.add(initLoggedUsersPanel(), BorderLayout.EAST);
        panelMain.add(initMessagePanel(), BorderLayout.SOUTH);

        add(panelMain);


    }

    private JPanel initLoginPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Username"));
        JTextField txtInputUserName = new JTextField("", 30);
        panel.add(txtInputUserName);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtInputUserName.getText();
                System.out.println("Button login clicked - " + userName);

                if(chatClient.isAuthenticated()){
                    //LOGOUT
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtInputUserName.setEditable(true);
                    txtInputMessage.setEnabled(false);
                    txtChatArea.setEnabled(false);
                }
                else {
                    //LOGIN
                    if(userName.length()<1){
                        JOptionPane.showMessageDialog(null,
                                "Enter your username",
                                "Error",
                                JOptionPane.WARNING_MESSAGE
                                );
                        return;
                    }
                    chatClient.login(userName);
                    btnLogin.setText("Logout");
                    txtInputUserName.setEditable(false);
                    txtInputMessage.setEnabled(true);
                    txtChatArea.setEnabled(true);
                }
            }
        });


        panel.add(btnLogin);

        return panel;
    }

    private JPanel initLoggedUsersPanel(){
        JPanel panel = new JPanel();

/*        Object[][] data = new Object[][]{
                {"0,0", "1,1"},
                {"1,0", "1,1"},
                {"xxxxxxx", "aaaaaa"}
        };
        String[] colNames = new String[] {"Col1", "Col2"};

        JTable tblLoggedUsers = new JTable(data, colNames);*/
        JTable tblLoggedUsers = new JTable();
        LoggedUsersTableModel loggedUsersTableModel = new LoggedUsersTableModel(chatClient);
        tblLoggedUsers.setModel(loggedUsersTableModel);

        chatClient.addActionListener(e -> {
            if(e.getID() == 1)
                loggedUsersTableModel.fireTableDataChanged();
        });

        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel initChatPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(txtChatArea);
        panel.add(scrollPane);

        /*for(int i = 0; i  < 50; i++){
            txtChatArea.append("Message " + i + "\n");
        }*/

        chatClient.addActionListener(e -> {
            if(e.getID() == 2)
                refreshMessages();
        });
        return panel;
    }

    private JPanel initMessagePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtInputMessage = new JTextField("", 50);
        txtInputMessage.setEnabled(false);
        panel.add(txtInputMessage);
        JButton btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(e -> {
            String msgText = txtInputMessage.getText();
            System.out.println("Btn send clicked - " + msgText);
//            txtChatArea.append(txtInputMessage.getText() + "\n");

            if(msgText.length() == 0){
                return;
            }
            if(!chatClient.isAuthenticated()){
                return;
            }
            chatClient.sendMessage(msgText);

            txtInputMessage.setText("");
            //refreshMessages();
        });
        panel.add(btnSendMessage);

        return panel;
    }

    private void refreshMessages(){
        if(!chatClient.isAuthenticated()){
            return;
        }
        txtChatArea.setText("");
        for(Message msg :
            chatClient.getMessages()){
            txtChatArea.append(msg.toString());
            txtChatArea.append("\n");
        }
    }
}
