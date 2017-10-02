/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cba.ca2.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trez__000
 */
public class Responder {

    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public void addClientHandler(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
    }

    public void respondToAllClients(ClientHandler sender, String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.equals(sender)) {
                clientHandler.getOutput().println(message);
            }
        }
    }

    public void respondToClientsByNames(ClientHandler sender, String message, List<String> receivers) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.equals(sender) && receivers.contains(clientHandler.getName())) {
                clientHandler.getOutput().println(message);
            }
        }
    }

    public void removeClientHandler(ClientHandler aThis) {
        clientHandlers.remove(aThis);
    }

    public String getLoggedInClientNames() {
        List<String> names = new ArrayList<>();
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isLoggedIn()) {
                names.add(clientHandler.getName());
            }
        }
        return String.join(",",names);
    }
}
