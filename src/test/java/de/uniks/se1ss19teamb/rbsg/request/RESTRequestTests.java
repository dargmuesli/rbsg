package de.uniks.se1ss19teamb.rbsg.request;

import org.junit.Assert;
import org.junit.Test;

public class RESTRequestTests {

	@Test
	public void registerUserRequestTest() {
		
		RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz");
		try {
			req.sendRequest((response) -> {
				Assert.assertEquals("failure", response.get("status"));
				Assert.assertEquals("Name already taken", response.get("message"));
			});
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		
	}
}
