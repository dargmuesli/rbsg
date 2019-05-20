package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class DeleteGameRequest extends AbstractRESTRequest{
   
   private String userToken, gameId;
   
   public DeleteGameRequest(String gameId, String userToken) {
      this.userToken = userToken;
      this.gameId = gameId;
   }
   
   @Override
   protected JSONObject buildJSON() {
      return null;
   }

   @Override
   protected String getHTTPMethod() {
      return "delete";
   }

   @Override
   protected String getEndpoint() {
      return "/game/" + gameId;
   }

   @Override
   protected String getUserToken() {
      return userToken;
   }

}
