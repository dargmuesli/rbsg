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

}


