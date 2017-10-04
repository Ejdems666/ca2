package org.cba.ca2.client.console;

import org.cba.ca2.client.ServerInputRunnable;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adam on 27/09/2017.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final Socket serverSocket = new Socket("localhost", 1235);
        executor.submit(new ServerOutputRunnable(serverSocket));
        executor.submit(new ServerInputRunnable(serverSocket, new ConsoleMessageHandler()));
        executor.shutdown();
    }


}
