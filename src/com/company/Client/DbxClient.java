package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;


public class DbxClient {
    private static final Logger LOGGER = Logger.getLogger(DbxClient.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            LOGGER.warning("Usage: java DbxClient <host name> <directory>");
            System.exit(1);
        }
        String hostName = args[0];
        String folderPath = args[1];

        try (Socket clientSocket = new Socket(hostName, 5555)) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(folderPath);
            out.println(hostName);
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            while (true) {

            }
        } catch (UnknownHostException e) {
            LOGGER.warning("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.warning("Couldn't get I/O for this connection to " + hostName);
            System.exit(1);
        }
    }
}
