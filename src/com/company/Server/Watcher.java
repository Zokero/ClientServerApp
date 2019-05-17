package com.company.Server;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        System.out.println("New file created " + newPath + " for " + userName);
                        Path fPath = Paths.get(path + "\\" + newPath);
                        System.out.println(Thread.currentThread().getId());
                        //Path sPath = Paths.get("E:\\git-repos\\ClientServerApp\\src\\com\\company\\Server\\ServerFolders\\1\\" + newPath);
                        Path sPath = Paths.get("C:\\Users\\pikuk1\\Documents\\git-repos\\ClientServerApp\\src\\com\\company\\Client\\ClientLocalFolders\\1\\" + newPath);
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void checkIfPathPointsToFolder(Path path){
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", LinkOption.NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path " + path + " is not a folder");
            }else{
                System.out.println("Path is pointing to folder directory");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
