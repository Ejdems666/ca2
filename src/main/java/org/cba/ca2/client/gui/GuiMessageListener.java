package org.cba.ca2.client.gui;

import org.cba.ca2.client.MessageListener;

import javax.swing.*;

/**
 * Created by adam on 03/10/2017.
 */
public class GuiMessageListener implements MessageListener {
    private final ChatBoxInput chatBoxInput;
    private final JComboBox<String> clientsSelectBox;

    public GuiMessageListener(ChatBoxInput chatBoxInput, JComboBox<String> clientsSelectBox) {
        this.chatBoxInput = chatBoxInput;
        this.clientsSelectBox = clientsSelectBox;
    }

    @Override
    public void handleIncomingMessage(String sender, String message) {
        chatBoxInput.printLineToChatBox(sender,message);
    }

    @Override
    public void handleEndConnection() {
        chatBoxInput.printLineToChatBox("server","done");
    }

    @Override
    public void handleClientListChange(String[] clientList) {
        clientsSelectBox.removeAllItems();
        for (String s : clientList) {
            clientsSelectBox.addItem(s);
        }
        clientsSelectBox.addItem("*");
    }
}
