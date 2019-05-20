package de.uniks.se1ss19teamb.rbsg.request;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import de.uniks.se1ss19teamb.rbsg.model.Game;

public class QueryGamesRequest extends AbstractRESTRequest {

   private String userToken;
   
   public QueryGamesRequest(String userToken) {
      this.userToken = userToken;
   }
   
   @Override
   protected JSONObject buildJSON() {
      return null;
   }

   @Override
   protected String getHTTPMethod() {
      return "get";
   }

   @Override
   protected String getEndpoint() {
      return "/game";
   }

   @Override
   protected String getUserToken() {
      return userToken;
   }
   
   //Custom Request Helper
   
   @SuppressWarnings("unchecked")
   public ArrayList<Game> getGames(){
      ArrayList<Game> games = new ArrayList<Game>();
      
      for(JSONObject game : (ArrayList<JSONObject>)getResponse().get("data")) {
         Game current = new Game();
         current.setId((String) game.get("id"));
         current.setName((String) game.get("name"));
         current.setJoinedPlayers((Long) game.get("joinedPlayer"));
         current.setNeededPlayers((Long) game.get("neededPlayer"));
         games.add(current);
      }
      
      return games;
   }

}
