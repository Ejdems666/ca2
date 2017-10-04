package org.cba.ca2.client.console;

import org.cba.ca2.client.MessageHandler;

/**
 * Created by adam on 03/10/2017.
 */
public class ConsoleMessageHandler implements MessageHandler {
    @Override
    public void handleIncomingMessage(String sender, String message) {
        System.out.println("<"+sender+">: "+message);
    }

    @Override
    public void handleEndConnection() {
        System.out.printf("done");
    }

    @Override
    public void handleClientListChange(String[] clientList) {
        System.out.println(clientList);
    }
}
