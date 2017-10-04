package org.cba.ca2.client.console;

import org.cba.ca2.client.Client;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by adam on 02/10/2017.
 */
public class ConsoleToServerHandler implements Runnable {
    final Client client;
    private String sendTo;

    public ConsoleToServerHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        PrintWriter serverOutput = client.getServerOutput();
        Scanner consoleInput = new Scanner(System.in);
        sendTo = "*";
        while (client.isConnected()) {
            processOutgoingMessage(consoleInput, sendTo);
        }
    }

    private void processOutgoingMessage(Scanner consoleInput, String sendTo) {
        String message = consoleInput.nextLine();
        String[] splitMessage = message.split(":");
        switch (splitMessage[0].toUpperCase()) {
            case "TO":
                sendTo = splitMessage[1];
                System.out.println("Sending messages to "+sendTo);
                break;
            case "LOGIN":
                client.sendLogin(splitMessage[1]);
                break;
            case "LOGOUT":
                client.sendLogout();
                break;
            default:
                client.sendMessage(sendTo,message);
        }
    }
}
