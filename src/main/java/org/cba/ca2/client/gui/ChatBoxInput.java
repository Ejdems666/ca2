package org.cba.ca2.client.gui;

import javax.swing.*;

/**
 * Created by adam on 03/10/2017.
 */
public class ChatBoxInput {
    private final JTextArea chatBox;

    public ChatBoxInput(JTextArea chatBox) {
        this.chatBox = chatBox;
    }

    public void printLineToChatBox(String sender,String text) {
        chatBox.append("<" + sender + ">:  " + text + "\n");
    }
}
