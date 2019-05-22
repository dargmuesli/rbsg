package de.uniks.se1ss19teamb.rbsg.Serialize;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class SerializeUtils{
  
  public static Object deserialization(String fileUrl, Object obj) {
    Gson gson = new Gson();
    try (Reader reader = new FileReader(fileUrl)) {
      // Convert JSON File to Java Object
      Object fromJson = gson.fromJson(reader, obj.getClass());
      // return object
      return fromJson;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return obj;
  }

  public static void serialization(String fileUrl, Object obj) {
    Gson gson = new Gson();
    try (FileWriter writer = new FileWriter(fileUrl)) {
      gson.toJson(obj, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}


