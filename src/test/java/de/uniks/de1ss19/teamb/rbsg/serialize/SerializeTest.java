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
    String name;
    int mtr;

    public void setMtr(int mtr) {
      this.mtr = mtr;
    }

    public int getMtr() {
      return mtr;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  @Test
  public void serializeTest() throws IOException {

    TestClass testClass = new TestClass();
    testClass.setName("My Very Long Name");
    testClass.setMtr(12345678);

    // test serialization
    SerializeUtils.serialize("file.json", testClass);

    // test json string
    String test = SerializeUtils.serialize(testClass);
    try {
      String thisTest = readFile("file.json", StandardCharsets.UTF_8);

      Assert.assertEquals(test,thisTest);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // other test
    Reader reader = new FileReader("file.json");
    Gson gson = new Gson();
    Object object = gson.fromJson(reader, testClass.getClass());

    TestClass thisTestClass = (TestClass) object;

    Assert.assertEquals(testClass.getName(), thisTestClass.getName());
    Assert.assertEquals(testClass.getMtr(), thisTestClass.getMtr());

    // test deserialization
    TestClass fromFile = (TestClass) SerializeUtils.deserialize("file.json", new TestClass());

    // test deserialization from string
    TestClass fromString = (TestClass) SerializeUtils.deserialize(test, new TestClass());

    Assert.assertEquals(testClass.getName(), fromFile.getName());
    Assert.assertEquals(testClass.getMtr(), fromFile.getMtr());
    Assert.assertEquals(testClass.getName(), fromString.getName());
    Assert.assertEquals(testClass.getMtr(), fromString.getMtr());

    reader.close();
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
}
