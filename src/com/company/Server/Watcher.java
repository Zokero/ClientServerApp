package com.company.Server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher {
    private static final Logger LOGGER = Logger.getLogger(Watcher.class.getName());

    public static void watchDirectory(String userName, Path path) {

        LOGGER.info("Check Path");
        checkIfPathPointsToFolder(path);
        LOGGER.info("Watching path " + path);
        FileSystem fileSystem = path.getFileSystem();

        try (WatchService watchService = fileSystem.newWatchService()) {
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            WatchKey key;
            while (true) {
                key = watchService.take();
                WatchEvent.Kind<?> kind;
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    kind = watchEvent.kind();
                    if (OVERFLOW == kind) {
                        continue;
                    } else if (ENTRY_CREATE == kind) {
                        Socket soc = new Socket(userName, 5555 );
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("New file created " + newPath + " for " + userName);
                        sendFileOnServer(soc, path + "\\" + newPath.toString(), newPath.toString());
                    } else if (ENTRY_DELETE == kind) {
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("Deleted file " + newPath + " for " + userName);
                    }
                }
                if (!key.reset()) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void checkIfPathPointsToFolder(Path path) {
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", LinkOption.NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path " + path + " is not a folder");
            } else {
                System.out.println("Path is pointing to folder directory");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendFileOnServer(Socket socket, String file, String name) throws IOException {
        System.out.println(file);
        File fileToSend = new File(file);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(fileToSend);
        byte[] buffer = new byte[(int)fileToSend.length()];
        dos.writeUTF(name);

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

        fis.close();
        dos.close();
        //socket.close();
    }
}
