package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;

public class UserData {
    
    public static void saveUserData(String userName, String password,
                                    String saveUsername, String savePassword) {
        SerializeUtils.serialize(saveUsername, userName);
        SerializeUtils.serialize(savePassword, password);
    }
    
    public static void loadUserData(String saveUsername, String savePassword, JFXCheckBox rememberLogin,
                                    JFXButton btnLogin, JFXTextField userName, JFXPasswordField password) {
        Path un = Paths.get(saveUsername);
        Path pw = Paths.get(savePassword);
        String userData = SerializeUtils.deserialize(un, saveUsername.getClass());
        String passwordData = SerializeUtils.deserialize(pw, savePassword.getClass());
        userName.setText(userData);
        password.setText(passwordData);
        rememberLogin.setSelected(true);
        Platform.runLater(() -> btnLogin.requestFocus());
    }
}
