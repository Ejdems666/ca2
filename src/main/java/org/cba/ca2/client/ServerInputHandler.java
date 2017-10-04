package org.cba.ca2.client;

import java.util.Scanner;

/**
 * Created by adam on 02/10/2017.
 */
public class ServerInputHandler implements Runnable {
    final Client client;
    final MessageListener messageListener;

    public ServerInputHandler(Client client, MessageListener messageListener) {
        this.client = client;
        this.messageListener = messageListener;
    }

    @Override
    public void run() {
        Scanner serverInput = client.getServerInput();
        while (client.isConnected()) {
            String rawMessage = serverInput.nextLine();
            try {
                processInput(rawMessage);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(rawMessage);
                e.printStackTrace();
            }
        }
        messageListener.handleEndConnection();
    }

    private void processInput(String rawMessage) throws IndexOutOfBoundsException{
        String[] splitMessage = rawMessage.split(":");
        switch (splitMessage[0].toUpperCase()) {
            case "MSGRES":
                messageListener.handleIncomingMessage(splitMessage[1], splitMessage[2]);
                break;
            case "CLIENTLIST":
                messageListener.handleClientListChange(splitMessage[1].split(","));
                break;
        }
    }
}
