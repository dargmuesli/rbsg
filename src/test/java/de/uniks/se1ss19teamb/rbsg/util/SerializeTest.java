package de.uniks.se1ss19teamb.rbsg.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;


public class SerializeTest {

    public class TestClass {
        String name = "My Very Long Name";
        int mtr = 12345678;
    }

    @Test
    public void serializeTest() throws IOException {

        TestClass testClass = new TestClass();

        // test serialization
        SerializeUtils.serialize("file.json", testClass);
        String serializedToJsonString = SerializeUtils.serialize(testClass);

        // test json string that should be equal to the serialized one
        String test = "{\"name\":\"My Very Long Name\",\"mtr\":12345678}";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("file.json"));
            String serializedToFile = new String(encoded, StandardCharsets.UTF_8);

            Assert.assertEquals(test, serializedToFile);
            Assert.assertEquals(test, serializedToJsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.delete(Paths.get("file.json"));
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", Paths.get("file.json"));
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", Paths.get("file.json"));
        } catch (IOException x) {
            // File permission problems are caught here.
            x.printStackTrace();
        }
    }

    @Test
    public void deserializeTest() {

        TestClass testClass = new TestClass();

        SerializeUtils.serialize("file.json", testClass);

        // test deserialization
        TestClass fromFile = SerializeUtils.deserialize(new File("file.json"), TestClass.class);

        // test deserialization from string
        String test = "{\"name\":\"My Very Long Name\",\"mtr\":12345678}";
        TestClass fromString = SerializeUtils.deserialize(test, TestClass.class);

        Assert.assertEquals(testClass.name, fromFile.name);
        Assert.assertEquals(testClass.mtr, fromFile.mtr);
        Assert.assertEquals(testClass.name, fromString.name);
        Assert.assertEquals(testClass.mtr, fromString.mtr);

        try {
            Files.delete(Paths.get("file.json"));
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
}
