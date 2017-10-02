/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cba.ca2.server;

/**
 *
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

    public PrintWriter getOutput() {
        return output;
    }

    @Override
    public void run() {
        try {
            Scanner input = new Scanner(client.getInputStream());
            output.println("Please Log in with command: \"LOGIN:name\"");
            String message = input.nextLine();
            while (!message.equalsIgnoreCase("exit")) {
                if (isLoggedIn()) {
                    processMessage(message);
                } else {
                    login(message);
                    if (isLoggedIn()) {
                        output.println("You're logged in as: " + name);
                        output.println("Clients online: "+responder.getLoggedinClientNames());
                        output.println("\"help\" for help");
                    } else {
                        output.println("You're not logged in! Try again");
                    }
                }

                message = input.nextLine();
            }
            responder.respondToAllClients(this, "Client " + name + " disconnected");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            responder.respondToAllClients(this, "Client " + name + " disconnected");
        }
        finally{
            responder.removeClientHandler(this);
        }

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
                name = null;
                output.println("You're logged out!");
                break;
            case "MSG":
                try {
                    if(splitMessage[1].isEmpty()){
                        output.println("Wrong Command!");
                        break;
                    }
                    String message = splitMessage[2];
                    if (splitMessage[1].equals("*")) {
                        responder.respondToAllClients(this, message);  
                        break;
                    }
                    
                    List<String> receivers = Arrays.asList(splitMessage[1].split(","));
                    responder.respondToClientsByNames(this, message, receivers);
                } catch (IndexOutOfBoundsException e) {
                    output.println("Wrong Command!");
                }
                break;
                
            case "CLIENTLIST":
                output.println(responder.getLoggedinClientNames());
                break;
            case "HELP":
                output.println("Commands: ");
                output.println("LOGOUT : logs you out ");
                output.println("MSG : write message to smb as \"MSG:client:message\"");
                output.println("CLIENTLIST : shows all online clients");
                output.println("EXIT : exit from chat");
                
                break;
                
            default:
                output.println("Wrong Command!");

        }
    }

}
