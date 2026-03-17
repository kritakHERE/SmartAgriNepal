package com.aurafarming.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtil {
    private FileUtil() {
    }

    public static void ensureAppDirectories() {
        ensureDirectory(Constants.DATA_DIR);
        ensureDirectory(Constants.EXPORT_DIR);
    }

    public static void ensureDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create directory: " + path, e);
        }
    }

    public static Path resolveDataFile(String fileName) {
        ensureAppDirectories();
        Path path = Constants.DATA_DIR.resolve(fileName);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create file: " + path, e);
            }
        }
        return path;
    }

    public static Path resolveExportFile(String fileName) {
        ensureAppDirectories();
        return Constants.EXPORT_DIR.resolve(fileName);
    }
}
