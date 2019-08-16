package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.request.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class RequestUtilTest {
    private HttpRequestResponse errorResponse =
        new HttpRequestResponse("{\"status\": \"error\"}", 200, null);
    private HttpRequestResponse successResponse =
        new HttpRequestResponse("{\"status\": \"success\", \"data\": {\"userKey\": \"123456\"}}", 200, null);

    @Start
    public void start(Stage stage) {
        AnchorPane apn = new AnchorPane();
        apn.setId("apnFade");
        stage.setScene(new Scene(apn));
        Main.PRIMARY_STAGE = stage;

        AbstractRestRequest.httpManager = mock(HttpManager.class);
    }

    @Test
    void requestBooleanTest() {
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
    void requestOptionalTest() {
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
