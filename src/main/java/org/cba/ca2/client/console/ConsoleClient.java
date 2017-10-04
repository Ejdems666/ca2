package org.cba.ca2.client.console;

import org.cba.ca2.client.Client;
import org.cba.ca2.client.ServerInputHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adam on 27/09/2017.
 */
public class ConsoleClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect("207.154.217.117",1235);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new ConsoleToServerHandler(client));
        executor.submit(new ServerInputHandler(client, new ConsoleMessageListener()));
        executor.shutdown();
    }
}
