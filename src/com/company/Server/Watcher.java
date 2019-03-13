package com.company.Server;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher {
    private static final Logger LOGGER = Logger.getLogger(Watcher.class.getName());

    public static void watchDirectory(String userName, Path path) {

        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", LinkOption.NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path " + path + " is not a folder");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        LOGGER.info("TEST");
        LOGGER.info("Watching path xxx " + path);
        FileSystem fs = path.getFileSystem();

        try (WatchService watchService = fs.newWatchService()) {
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
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("New file created " + newPath + " for " + userName);
                        Path fPath = Paths.get(path + "\\" + newPath);
                        Path sPath = Paths.get("E:\\git-repos\\ClientServerApp\\src\\com\\company\\Server\\ServerFolders\\1\\" + newPath);
                        Files.copy(fPath, sPath, REPLACE_EXISTING);
                    } else if (ENTRY_MODIFY == kind) {
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("Modified file " + newPath + " for " + userName);
                    } else if (ENTRY_DELETE == kind) {
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("Deleted file " + newPath + " for " + userName);
                    }
                }
                if (!key.reset()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
    }
}
