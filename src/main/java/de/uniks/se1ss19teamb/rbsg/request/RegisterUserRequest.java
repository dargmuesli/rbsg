package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class RegisterUserRequest extends AbstractRESTRequest{
   
   private String username, password;
   
   /**
    * Returns an empty "data" array, a "status" ("success" | "failure"), and a human readable "message"
    * 
    * @param username The Username to be used
    * @param password The Password to be associated with the user
    */
   public RegisterUserRequest(String username, String password) {
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
      return "/user";
   }

   @Override
   protected String getUserToken() {
      return null;
   }
}
