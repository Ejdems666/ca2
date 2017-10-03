package org.cba.ca2.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GUIClient {

    String      appName     = "SOCKET CHAT 0.1V";
    JFrame newFrame = new JFrame("Colt Chat v0.1");
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameChooser;
    JFrame preFrame;
    JTextField  ipAddress;
    JTextField  port;
    private Socket serverSocket;
    private PrintWriter serverOutput;
    private Scanner serverInput;

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        GUIClient GUIClient = new GUIClient();
        GUIClient.renderLoginFrame();
    }

    public void renderLoginFrame() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        usernameChooser = new JTextField(25);
        ipAddress = new JTextField(25);
        port = new JTextField(25);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JLabel chooseIpAddress = new JLabel("Insert IP address:");
        JLabel choosePort = new JLabel("Insert Port");
        JButton enterServer = new JButton("Enter Chat Server");
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        prePanel.add(chooseIpAddress, preLeft);
        prePanel.add(ipAddress, preRight);
        prePanel.add(choosePort, preLeft);
        prePanel.add(port, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(600, 600);
        preFrame.setVisible(true);
        preFrame.getRootPane().setDefaultButton(enterServer);

        enterServer.addActionListener(new enterServerButtonListener());
    }

    public void renderChatFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(40);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 25));
        chatBox.setLineWrap(true);
        printLineToChatBox("server","You're logged in as"+username);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(870, 500);
        newFrame.setVisible(true);
        newFrame.getRootPane().setDefaultButton(sendMessage);

        new Thread(() -> {
            try {
                Scanner serverInput = new Scanner(serverSocket.getInputStream());
                while (serverSocket.isConnected()) {
                    String message = serverInput.nextLine();
                    System.out.println(message);
                    String[] splitMessage = message.split(":");
                    if (splitMessage[0].equalsIgnoreCase("MSGRES")) {
                        printLineToChatBox(splitMessage[1],splitMessage[2]);
                    }
                }
                printLineToChatBox("server","done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            printLineToChatBox(username,messageBox.getText());
            serverOutput.println("MSG:*:"+messageBox.getText());
            messageBox.setText("");
        }
    }

    private void printLineToChatBox(String sender,String text) {
        chatBox.append("<" + sender + ">:  " + text + "\n");
    }

    String username;

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            String ip = ipAddress.getText();
            int portNumber = Integer.parseInt(port.getText());
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            }
            else {
                try {
                    connectToServer(ip,portNumber);
                    serverOutput.println("LOGIN:"+username);
                    preFrame.setVisible(false);
                    renderChatFrame();
                } catch (IOException e) {
                    System.out.println("Couldn't connect to: "+ip+":"+portNumber);
                }
            }
        }

    }

    private void connectToServer(String ip, int port) throws IOException {
        serverSocket = new Socket(ip, port);
        serverOutput = new PrintWriter(serverSocket.getOutputStream(), true);
        serverInput = new Scanner(serverSocket.getInputStream());
    }
}