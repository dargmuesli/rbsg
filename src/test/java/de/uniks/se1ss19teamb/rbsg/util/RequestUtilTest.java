package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uniks.se1ss19teamb.rbsg.request.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestUtilTest {
    private HttpRequestResponse errorResponse =
        new HttpRequestResponse("{\"status\": \"error\"}", 200, null);
    private HttpRequestResponse successResponse =
        new HttpRequestResponse("{\"status\": \"success\", \"data\": {\"userKey\": \"123456\"}}", 200, null);

    @Before
    public void before() {
        AbstractRestRequest.httpManager = mock(HttpManager.class);
    }

    @Test
    public void requestBooleanTest() {
        try {
            when(AbstractRestRequest.httpManager.get(any(), any())).thenReturn(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(RequestUtil.request(new LogoutUserRequest(null)));

        try {
            when(AbstractRestRequest.httpManager.get(any(), any())).thenReturn(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertFalse(RequestUtil.request(new LogoutUserRequest(null)));
    }

    @Test
    public void requestOptionalTest() {
        try {
            when(AbstractRestRequest.httpManager.post(any(), any(), any())).thenReturn(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(RequestUtil.request(new LoginUserRequest(null, null)).isPresent());

        try {
            when(AbstractRestRequest.httpManager.post(any(), any(), any())).thenReturn(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertFalse(RequestUtil.request(new LoginUserRequest(null, null)).isPresent());
    }
}
