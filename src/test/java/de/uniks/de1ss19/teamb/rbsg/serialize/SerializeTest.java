package de.uniks.de1ss19.teamb.rbsg.serialize;

import com.google.gson.Gson;
import de.uniks.se1ss19teamb.rbsg.serialize.SerializeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

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
      System.err.println(x);
    }
  }

  @Test
  public void deserializeTest() throws IOException {

    TestClass testClass = new TestClass();

    SerializeUtils.serialize("file.json", testClass);

    // test deserialization
    TestClass fromFile = SerializeUtils.deserialize("file.json", TestClass.class);

    // test deserialization from string
    String test = "{\"name\":\"My Very Long Name\",\"mtr\":12345678}";
    TestClass fromString = SerializeUtils.deserializeFromJsonString(test, TestClass.class);

    Assert.assertEquals(testClass.name, fromFile.name);
    Assert.assertEquals(testClass.mtr, fromFile.mtr);
    Assert.assertEquals(testClass.name, fromString.name);
    Assert.assertEquals(testClass.mtr, fromString.mtr);

    try {
      Files.delete(Paths.get("file.json"));
    } catch (NoSuchFileException x) {
      x.printStackTrace();
    } catch (DirectoryNotEmptyException x) {
      x.printStackTrace();
    } catch (IOException x) {
      // File permission problems are caught here.
      x.printStackTrace();
    }
  }
}
