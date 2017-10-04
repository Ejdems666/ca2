package org.cba.ca2.client.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by adam on 02/10/2017.
 */
public class ServerOutputRunnable implements Runnable {
    final Socket serverSocket;

    public ServerOutputRunnable(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter serverOutput = new PrintWriter(serverSocket.getOutputStream(), true);
            Scanner consoleInput = new Scanner(System.in);
            while (serverSocket.isConnected()) {
                String message = consoleInput.nextLine();
                serverOutput.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
