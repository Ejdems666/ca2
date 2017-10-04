package org.cba.ca2.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by adam on 04/10/2017.
 */
public class Client {
    private Socket serverSocket;
    private Scanner serverInput;
    private PrintWriter serverOutput;

    public Socket connect(String ip, int port) throws IOException {
        serverSocket = new Socket(ip, port);
        serverInput = new Scanner(serverSocket.getInputStream());
        serverOutput = new PrintWriter(serverSocket.getOutputStream(),true);
        return serverSocket;
    }

    public boolean isConnected() {
        return serverSocket != null && serverSocket.isConnected();
    }

    public void disconnect() throws IOException {
        serverSocket.close();
    }

    public void sendMessage(String[] recievers, String message) {
        sendMessage(String.join(",",recievers),message);
    }
    public void sendMessage(String recievers, String message) {
        serverOutput.println("MSG:"+recievers+":"+message);
    }
    public void sendMessageToAll(String message){
        serverOutput.println("MSG:*:"+message);
    }
    public void sendLogin(String name) {
        serverOutput.println("LOGIN:"+name);
    }
    public void sendLogout(){
        serverOutput.println("LOGOUT");
    }

    public Scanner getServerInput() {
        return serverInput;
    }

    public PrintWriter getServerOutput() {
        return serverOutput;
    }
}
