package com.company.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Client connected on port " + portNumber + " Servicing request");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received message " + inputLine + " from " + socket.toString());
                out.println(inputLine);
            }


        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port"
                    + portNumber + " or listening for a connection");
        }
    }
}

