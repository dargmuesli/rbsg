package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.testmodel.Game; //TODO needs to be changed when we implement the game

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
import org.fulib.yaml.YamlIdMap;


public class PersistenceUtil {

    public static final String SAVEGAME_YAML = "./savegame.yaml";
    private YamlIdMap yamlIdMap = new YamlIdMap(Game.class.getPackage().getName());

    public void save(Game game) {

        String yaml = yamlIdMap.encode(game);

        try {

            File file = new File(SAVEGAME_YAML);

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(file.toURI()), yaml.getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game load() {
        File file = new File(SAVEGAME_YAML);

        if (!file.exists()) {
            return null;
        }

        Game game = new Game();

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
            String yaml = new String(bytes, "UTF-8");
            yamlIdMap.decode(yaml, game);
            return game;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Game loader() {
        PersistenceUtil p = new PersistenceUtil();
        return p.load();
    }
    
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
