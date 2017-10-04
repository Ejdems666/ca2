package org.cba.ca2.server;

import java.util.ArrayList;
import java.util.List;

public class Responder {

    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public void addClientHandler(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
    }

    /**
     * Sends the message to all clients in the correct format: MSGRES:<senderName>:<message>
     */
    public void sendMessageToAllClients(ClientHandler sender, String message) {
        message = getFormattedMessage(sender, message);
        sendRawMessageToAllClients(message);
    }

    private String getFormattedMessage(ClientHandler sender, String message) {
        return "MSGRES:" + sender.getName() + ":" + message;
    }

    /**
     * Sends the message to clients just as it is
     */
    private void sendRawMessageToAllClients(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.getOutput().println(message);
        }
    }

    /**
     * Sends the message to specified recipients in the correct format: MSGRES:<senderName>:<message>
     */
    public void sendMessageToClientsByNames(ClientHandler sender, List<String> recipients, String message) {
        message = getFormattedMessage(sender, message);
        for (ClientHandler clientHandler : clientHandlers) {
            if (recipients.contains(clientHandler.getName())) {
                clientHandler.getOutput().println(message);
            }
        }
    }

    public void sendClientListToAllClients() {
        sendRawMessageToAllClients("CLIENTLIST:" + getLoggedInClientNames());
    }

    private String getLoggedInClientNames() {
        List<String> names = new ArrayList<>();
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isLoggedIn()) {
                names.add(clientHandler.getName());
            }
        }
        return String.join(",", names);
    }

    public void removeClient(ClientHandler initiator) {
        clientHandlers.remove(initiator);
    }
}
