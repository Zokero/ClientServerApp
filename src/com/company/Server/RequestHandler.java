package com.company.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandler implements Runnable{
    private final Socket client;
    ServerSocket serverSocket = null;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))){
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
            String userInput;
            while ((userInput = in.readLine()) != null){
                userInput = userInput.replaceAll("[^A-Za-z0-9]", "");
                System.out.println("Received mesage from " + Thread.currentThread().getName() + " : " + userInput);
                out.write("You entered : " + userInput);
                out.newLine();
                out.flush();
            }
        }catch (IOException e){
            System.out.println("I/O exception: " + e);

        }catch (Exception ex){
            System.out.println("Exception in Thread Run, Exception : " + ex);
        }
    }
}
