package de.uniks.se1ss19teamb.rbsg.request;

import java.io.IOException;
import java.net.URI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;


import de.uniks.se1ss19teamb.rbsg.HTTPManager;

public abstract class AbstractRESTRequest implements RESTRequest{
   
   private static final String url = "https://rbsg.uniks.de/api";
   
   private JsonObject response = null;
   
   private static HTTPManager httpManager = new HTTPManager();
   
   protected abstract JsonObject buildJson();
   
   protected abstract String getHTTPMethod(); //"get", "post", "delete", "put"
   
   protected abstract String getEndpoint(); //"/user", "/user/login", etc.
   
   protected abstract String getUserToken();
   
   @Override
   public void sendRequest() throws IOException, ParseException {
      HTTPRequestResponse result = null;
      String token = getUserToken();
      try {
         switch(getHTTPMethod()) {
         case "get":
            result = httpManager.get(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) });
            break;
         case "post":
            result = httpManager.post(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) }, new StringEntity(buildJson().toString()));
            break;
         case "delete":
            result = httpManager.delete(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) }, null);
            break;
            
         default:
            throw new MethodNotSupportedException("Method not Supported: " + getHTTPMethod());
         }
      } catch(Exception e) {
         throw new IOException(e);
      } finally {
         JsonParser parser = new JsonParser();
         response = (JsonObject) parser.parse(result == null ? "" : result.body);
         
         //TODO Handle result status codes
      }
   }
   
   @Override
   public void sendRequest(RESTRequestResponseHandler callback) throws IOException, ParseException {

      sendRequest();
      
      callback.handle(response);
   }
   
   @Override
   public JsonObject getResponse() {
      return response;
   }
   
}
