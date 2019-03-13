package com.company.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageServer {

    public static void main(String[] args){
        if (args.length < 1) {
            System.err.println("Usage: java StorageServer <port number>");
            System.exit(1);
        }
        System.out.println("Server started. Listening on Port 5555");
        int portNumber = Integer.parseInt(args[0]);
        ExecutorService executor = null;

        try (ServerSocket listener = new ServerSocket(portNumber)) {
            executor = Executors.newFixedThreadPool(5);
            System.out.println("StorageServer is running...");
            while(true){
                Socket socket = listener.accept();
                Runnable worker = new RequestHandler(socket);
                executor.execute(worker);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port"
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }finally {
            if(executor != null){
                executor.shutdown();
            }
        }
    }
}

