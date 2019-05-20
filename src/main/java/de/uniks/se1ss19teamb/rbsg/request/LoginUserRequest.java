package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class LoginUserRequest extends AbstractRESTRequest{
   
   private String username, password;
   
   /**
    * Returns an empty "data" array, a "status" ("success" | "failure"), and a human readable "message"
    * 
    * @param username The User to be logged in
    * @param password The Password associated with the user
    */
   public LoginUserRequest(String username, String password) {
      this.username = username;
      this.password = password;
   }
   
   @SuppressWarnings("unchecked")
   @Override
   protected JSONObject buildJSON() {
      JSONObject json = new JSONObject();
      json.put("name", username);
      json.put("password", password);
      return json;
   }

   @Override
   protected String getHTTPMethod() {
      return "post";
   }

   @Override
   protected String getEndpoint() {
      return "/user/login";
   }

   @Override
   protected String getUserToken() {
      return null;
   }
   
   //Custom Request Helpers
   public String getUserKey() {
      return ((String) ((JSONObject)getResponse().get("data")).get("userKey"));
   }

}
