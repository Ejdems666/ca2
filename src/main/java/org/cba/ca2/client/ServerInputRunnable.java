package org.cba.ca2.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by adam on 02/10/2017.
 */
public class ServerInputRunnable implements Runnable {
    final Socket serverSocket;
    final MessageHandler messageHandler;

    public ServerInputRunnable(Socket serverSocket, MessageHandler messageHandler) {
        this.serverSocket = serverSocket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            Scanner serverInput = new Scanner(serverSocket.getInputStream());
            while (serverSocket.isConnected()) {
                String rawMessage = serverInput.nextLine();
                String[] splitMessage = rawMessage.split(":");
                switch (splitMessage[0].toUpperCase()) {
                    case "MSGRES":
                        messageHandler.handleIncomingMessage(splitMessage[1], splitMessage[2]);
                        break;
                    case "CLIENTLIST":
                        messageHandler.handleClientListChange(splitMessage[1].split(","));
                        break;
                }
            }
            messageHandler.handleEndConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
