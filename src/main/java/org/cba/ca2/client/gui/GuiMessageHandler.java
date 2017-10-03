package org.cba.ca2.client.gui;

import org.cba.ca2.client.MessageHandler;

/**
 * Created by adam on 03/10/2017.
 */
public class GuiMessageHandler implements MessageHandler {
    private final ChatBoxInput chatBoxInput;

    public GuiMessageHandler(ChatBoxInput chatBoxInput) {
        this.chatBoxInput = chatBoxInput;
    }

    @Override
    public void handleIncomingMessage(String sender, String message) {
        System.out.println(message);
        chatBoxInput.printLineToChatBox(sender,message);
    }

    @Override
    public void handleEndConnection() {
        chatBoxInput.printLineToChatBox("server","done");
    }

    @Override
    public void handleClientListChange(String[] clientList) {
        System.out.println(clientList);
    }
}
