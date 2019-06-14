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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
            String userInput;

            while ((userInput = in.readLine()) != null) {
                out.write("OK");
                out.newLine();
                System.out.println(Thread.currentThread().getName() + " : " + userInput);
                out.write("Watching directory : " + userInput);
                out.newLine();
                out.flush();
                saveFile(client);
            }
        } catch (IOException e) {
            System.out.println("I/O exception: " + e);
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream(dis.readUTF());
        byte[] buffer = new byte[4096];

        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        fos.flush();
        dis.close();
    }
}
