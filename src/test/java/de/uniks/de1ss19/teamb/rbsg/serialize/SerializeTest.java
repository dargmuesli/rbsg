package de.uniks.de1ss19.teamb.rbsg.serialize;

import com.google.gson.Gson;
import de.uniks.se1ss19teamb.rbsg.serialize.SerializeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class SerializeTest {

  public class TestClass{
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

  @Test
  public void serializeTest() throws FileNotFoundException {

    TestClass testClass = new TestClass();
    testClass.setName("My Very Long Name");
    testClass.setMtr(12345678);

    // test serialization
    SerializeUtils.serialize("file.json", testClass);

    Reader reader = new FileReader("file.json");
    Gson gson = new Gson();
    Object object = gson.fromJson(reader, testClass.getClass());

    TestClass thisTestClass = (TestClass) object;

    Assert.assertEquals(testClass.getName(), thisTestClass.getName());
    Assert.assertEquals(testClass.getMtr(), thisTestClass.getMtr());

    // test deserialization
    TestClass otherTestClass =(TestClass) SerializeUtils.deserialize("file.json", new TestClass());

    Assert.assertEquals(testClass.getName(), otherTestClass.getName());
    Assert.assertEquals(testClass.getMtr(), otherTestClass.getMtr());
    System.out.println();
  }
}
