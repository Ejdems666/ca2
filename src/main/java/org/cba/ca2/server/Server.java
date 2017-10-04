/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cba.ca2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author trez__000
 */
public class Server {
    private static ServerSocket serverSocket;
    private static String IP = "localhost";
    private static int port = 1235;
    
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            IP = args[1];
        }
        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for clients");
        Responder responder = new Responder();
        ExecutorService es = Executors.newFixedThreadPool(5);
        while (true) {
            Socket clientSocket = serverSocket.accept();           
            System.out.println("ConsoleClient connected");
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            responder.addClientHandler(clientHandler);
            clientHandler.setResponder(responder);
            es.submit(clientHandler);
        }
        
        
    }
    
    
}
