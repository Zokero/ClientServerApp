package com.company.Server;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
        try {
            saveFile(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream(dis.readUTF());
        byte[] buffer = new byte[1024];

        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while ((read = dis.read(buffer)) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        fos.close();
        dis.close();
        //clientSock.close();
    }
}
