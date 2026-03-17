package com.aurafarming.dao;

import com.aurafarming.util.FileUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseObjectDAO<T extends Serializable> {
    private final String fileName;

    protected BaseObjectDAO(String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("unchecked")
    protected List<T> readAllInternal() {
        Path path = FileUtil.resolveDataFile(fileName);
        File file = path.toFile();
        if (file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = inputStream.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            }
            return new ArrayList<>();
        } catch (EOFException ignored) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to read file data for: " + fileName, e);
        }
    }

    protected void writeAllInternal(List<T> list) {
        Path path = FileUtil.resolveDataFile(fileName);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            outputStream.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file data for: " + fileName, e);
        }
    }
}
