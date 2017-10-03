package org.cba.ca2.client;

/**
 * Created by adam on 03/10/2017.
 */
public interface MessageHandler {
    void handleIncomingMessage(String sender, String message);
    void handleEndConnection();
    void handleClientListChange(String[] clientList);
}
