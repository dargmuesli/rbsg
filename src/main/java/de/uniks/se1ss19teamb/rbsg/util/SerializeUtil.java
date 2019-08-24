package de.uniks.se1ss19teamb.rbsg.util;

import com.google.gson.Gson;
import java.io.*;
import java.util.Optional;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializeUtil {
    private static final Logger logger = LogManager.getLogger();

    // Source: https://stackoverflow.com/a/10055962/4682621
    private static class FolderFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isDirectory();
        }

        @Override
        public String getDescription() {
            return "Directories";
        }
    }

    public static Optional<File> chooseFile(boolean foldersOnly) {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();

        if (foldersOnly) {
            fileChooser.setDialogTitle("Select a folder");
            fileChooser.setFileFilter(new FolderFilter());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            fileChooser.setDialogTitle("Select a file");
        }

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        } else {
            return Optional.empty();
        }
    }

    public static <T> T deserialize(File file, Class<T> myClass) {
        try (Reader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, myClass);
        } catch (IOException e) {
            NotificationHandler.sendError(
                "Could not deserialize " + file.getName() + " to " + myClass.getName() + "!", logger, e);
        }

        return null;
    }

    public static <T> T deserialize(String jsonString, Class<T> clazz) {
        return new Gson().fromJson(jsonString, clazz);
    }

    public static <T> void serialize(String fileString, T object) {
        try (FileWriter writer = new FileWriter(fileString)) {
            new Gson().toJson(object, writer);
        } catch (IOException e) {
            NotificationHandler.sendError(
                "Could not serialize " + object + " to " + fileString + "!", logger, e);
        }
    }

    public static <T> String serialize(T object) {
        return new Gson().toJson(object);
    }
}