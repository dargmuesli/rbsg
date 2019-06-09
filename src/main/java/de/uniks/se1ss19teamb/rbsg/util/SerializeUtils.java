package de.uniks.se1ss19teamb.rbsg.util;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SerializeUtils {

    public static <T> T deserialize(File file, Class<T> myClass) {
        try (Reader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, myClass);
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public static <T> String serialize(T object) {
        return new Gson().toJson(object);
    }

}