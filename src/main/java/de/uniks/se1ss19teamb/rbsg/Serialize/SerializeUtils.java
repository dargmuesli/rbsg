package de.uniks.se1ss19teamb.rbsg.Serialize;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/* TODO 1
 *   complete Serializatioon test for all the Objects in the game
 * */

public class SerializeUtils{

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

  public static Game deSerializeGame(String file, Game game){
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

}


