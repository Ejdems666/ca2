package org.cba.ca2.client.gui;

import org.cba.ca2.client.MessageListener;

import javax.swing.*;

/**
 * Created by adam on 03/10/2017.
 */
public class GuiMessageListener implements MessageListener {
    private final ChatBoxInput chatBoxInput;
    private final JComboBox<String> clientsSelectBox;
    private final String username;

    public GuiMessageListener(ChatBoxInput chatBoxInput, JComboBox<String> clientsSelectBox, String username) {
        this.chatBoxInput = chatBoxInput;
        this.clientsSelectBox = clientsSelectBox;
        this.username = username;
    }

    @Override
    public void handleIncomingMessage(String sender, String message) {
        if(!sender.equals(username)) chatBoxInput.printLineToChatBox(sender,message);
    }

    @Override
    public void handleEndConnection() {
        chatBoxInput.printLineToChatBox("server","done");
    }

    @Override
    public void handleClientListChange(String[] clientList) {
        clientsSelectBox.removeAllItems();
        for (String s : clientList) {
            if (!s.equals(username)) clientsSelectBox.addItem(s);
        }
        clientsSelectBox.addItem("*");
    }
}
