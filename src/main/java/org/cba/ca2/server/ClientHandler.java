/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cba.ca2.server;

/**
 * @author trez__000
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final PrintWriter output;
    private String name;

    public String getName() {
        return name;
    }

    private Responder responder;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        output = new PrintWriter(client.getOutputStream(), true);
    }

    public void setResponder(Responder responder) {
        this.responder = responder;
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void run() {
        try {
            Scanner input = new Scanner(client.getInputStream());
            String message = input.nextLine();
            while (!message.equalsIgnoreCase("EXIT")) {
                if (isLoggedIn()) {
                    processMessage(message);
                } else {
                    login(message);
                    if (isLoggedIn()) {
                        responder.sendClientListToAllClients();
                    } else {
                        logError("You're not logged in! Try again");
                    }
                }
                message = input.nextLine();
            }
            client.close();
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            if (isLoggedIn()) {
                logoutAndNotify();
            }
            responder.removeClient(this);
        }
    }

    private void logoutAndNotify() {
        name = null;
        responder.sendClientListToAllClients();
    }

    private void logError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public boolean isLoggedIn() {
        return name != null;
    }

    private void login(String message) {
        String[] splitMessage = message.split(":");
        if (splitMessage.length == 2 && splitMessage[0].equals("LOGIN")) {
            name = splitMessage[1];
        }

    }

    private void processMessage(String rawMessage) {
        String[] splitMessage = rawMessage.split(":", 3);
        switch (splitMessage[0].toUpperCase()) {
            case "LOGOUT":
                logoutAndNotify();
                break;
            case "MSG":
                try {
                    if (splitMessage[1].isEmpty()) {
                        logError("Wrong Command!");
                        break;
                    }
                    String message = splitMessage[2];
                    if (splitMessage[1].equals("*")) {
                        responder.sendMessageToAllClients(this, message);
                        break;
                    }

                    List<String> receivers = Arrays.asList(splitMessage[1].split(","));
                    responder.sendMessageToClientsByNames(this, receivers, message);
                } catch (IndexOutOfBoundsException e) {
                    logError("Wrong Command!");
                }
                break;
            default:
                logError("Wrong Command!");

        }
    }

}
