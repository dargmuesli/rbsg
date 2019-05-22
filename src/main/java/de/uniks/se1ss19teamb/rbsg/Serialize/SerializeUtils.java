package de.uniks.se1ss19teamb.rbsg.serialize;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class SerializeUtils{

  /*
  public class Game {

    //Fields defined by Server
    private long joinedPlayers;
    private String name;
    private String id;
    private long neededPlayers;

    public long getJoinedPlayers() {
      return joinedPlayers;
    }
    public void setJoinedPlayers(Long joinedPlayers) {
      this.joinedPlayers = joinedPlayers;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public long getNeededPlayers() {
      return neededPlayers;
    }
    public void setNeededPlayers(long neededPlayers) {
      this.neededPlayers = neededPlayers;
    }
  }

  // Method 1
  public static void serializeGame(String file, Game game){
    JSONObject json = new JSONObject();
    json.put("joinedPlayers", game.getJoinedPlayers());
    json.put("name", game.getName());
    json.put("id", game.getId());
    json.put("neededPlayers", game.getNeededPlayers());

    try{
      FileWriter jsonFileWriter = new FileWriter(file);
      jsonFileWriter.write(json.toJSONString());
      jsonFileWriter.flush();
      jsonFileWriter.close();
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public static Game deSerializeGameFromFile(String file, Game game){
    JSONParser parser = new JSONParser();

    try {
      FileReader fileReader = new FileReader(file);
      JSONObject json = (JSONObject) parser.parse(fileReader);

      long joinedPlayers = (long) json.get("joinedPlayers");
      String name = (String) json.get("name");
      String id = (String) json.get("id");
      long neededPlayers = (long) json.get("neededPlayers");

      game.setJoinedPlayers(joinedPlayers);
      game.setName(name);
      game.setId(id);
      game.setNeededPlayers(neededPlayers);
    } catch (Exception ex){
      ex.printStackTrace();
    }
    return game;
  }
  */

  public static Object deserialize(String fileUrl, Object obj) {
    Gson gson = new Gson();
    try (Reader reader = new FileReader(fileUrl)) {
      Object fromJson = gson.fromJson(reader, obj.getClass());
      return fromJson;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return obj;
  }

  public static void serialize(String fileUrl, Object obj) {
    Gson gson = new Gson();
    try (FileWriter writer = new FileWriter(fileUrl)) {
      gson.toJson(obj, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}


