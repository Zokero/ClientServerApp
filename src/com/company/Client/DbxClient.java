package com.company.Client;

import com.company.Server.Watcher;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class DbxClient {
    private static final Logger LOGGER = Logger.getLogger(DbxClient.class.getName());

    public static void main(String[] args) {
        if (args.length != 2) {
            LOGGER.warning("Usage: java DbxClient <host name> <directory>");
            System.exit(1);
        }
        String hostName = args[0];
        String folderPath = args[1];
        //try (Socket clientSocket = new Socket(hostName, 5555)) {
            //clientSocket.setKeepAlive(true);
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//            out.println(folderPath);
//            out.println(hostName);
//            System.out.println(in.readLine());
            Watcher.watchDirectory(hostName, Paths.get(folderPath));
            // while (true) {

            // }
//        } catch (UnknownHostException e) {
//            LOGGER.warning("Don't know about host " + hostName);
//            System.exit(1);
//        } catch (IOException e) {
//            LOGGER.warning("Couldn't get I/O for this connection to " + hostName);
//            System.exit(1);
//        }
    }
}
