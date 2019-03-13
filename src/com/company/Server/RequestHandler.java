package com.company.Server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler implements Runnable {
    private final Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
            String userInput;
            while ((userInput = in.readLine()) != null) {
                String hostName = in.readLine();
                out.write("OK");
                out.newLine();
                System.out.println("Received mesage from " + Thread.currentThread().getName() + " : " + userInput);
                out.write("Watching directory : " + userInput);
                out.newLine();
                out.flush();
                Path path = Paths.get(userInput);
                Watcher.watchDirectory(hostName, path);
            }
        } catch (IOException e) {
            System.out.println("I/O exception: " + e);
        } catch (Exception ex) {
            System.out.println("Exception in Thread Run, Exception : " + ex);
        }
    }
}
