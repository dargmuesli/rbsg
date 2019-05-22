package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class CreateGameRequest extends AbstractRESTRequest{
   
   private String userToken, gameName;
   private int neededPlayers;
   
   public CreateGameRequest(String gameName, int neededPlayers, String userToken) {
      this.userToken = userToken;
      this.neededPlayers = neededPlayers;
      this.gameName = gameName;
   }
   
   @SuppressWarnings("unchecked")
   @Override
   protected JSONObject buildJSON() {
      JSONObject json = new JSONObject();
      json.put("name", gameName);
      json.put("neededPlayer", neededPlayers);
      return json;
   }

   @Override
   protected String getHTTPMethod() {
      return "post";
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
   
   public String getGameId() {
      return ((String) ((JSONObject)getResponse().get("data")).get("gameId"));
   }

}
