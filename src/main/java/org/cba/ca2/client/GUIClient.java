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

    JFrame newFrame = new JFrame("Colt Chat v0.1");
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameChooser;
    JFrame preFrame;
    private Socket serverSocket;
    private PrintWriter serverOutput;
    private final Scanner serverInput;

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        GUIClient GUIClient = new GUIClient();
        GUIClient.renderLoginFrame();
    }

    public GUIClient() throws IOException {
        serverSocket = new Socket("localhost", 1235);
        serverOutput = new PrintWriter(serverSocket.getOutputStream(), true);
        serverInput = new Scanner(serverSocket.getInputStream());
    }

    public void renderLoginFrame() {
        newFrame.setVisible(false);
        preFrame = new JFrame("Choose your username!(Colt chat v0.1");
        usernameChooser = new JTextField();
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setVisible(true);
        preFrame.setSize(300, 300);
        preFrame.getRootPane().setDefaultButton(enterServer);

        enterServer.addActionListener(new enterServerButtonListener(serverOutput));
    }

    public void renderChatFrame() {
        newFrame.setVisible(true);
        JPanel southPanel = new JPanel();
        newFrame.add(BorderLayout.SOUTH, southPanel);
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        sendMessage = new JButton("Send Message");
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        newFrame.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        chatBox.setLineWrap(true);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.EAST;
        right.weightx = 2.0;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.append("You're logged in as "+username+"\n");
        sendMessage.addActionListener(new sendMessageButtonListener(serverOutput));
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
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
        private final PrintWriter serverOutput;

        sendMessageButtonListener(PrintWriter serverOutput) {
            this.serverOutput = serverOutput;
        }

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
        private final PrintWriter serverOutput;

        enterServerButtonListener(PrintWriter serverOutput) {
            this.serverOutput = serverOutput;
        }

        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            }
            else {
                serverOutput.println("LOGIN:"+username);
                preFrame.setVisible(false);
                renderChatFrame();
            }
        }

    }
}