package de.uniks.se1ss19teamb.rbsg.ui;

import static org.junit.Assert.assertEquals;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;


public class HealthBarTest {
    private EnvironmentTile eT = new EnvironmentTile();
    private InGamePlayer player = new InGamePlayer();
    private UnitTile uT = new UnitTile();
    private ArrayList<UnitTile> unitTiles = new ArrayList<>();
    private List<Pane> unitPanes = new ArrayList<>();
    private InGameController inGameController = new InGameController();

    //UnitTile parampeters
    private String game = "MyTestGame";
    private String type = "Choppper";
    private String testId = "12345";
    private int mp = 20;
    private int hp = 10;
    private String[] canAttack = {"JEEP", "BAZOOKA TROPPER", "INFANTRY"};
    private String posiontion = "POSITION";
    
    //InGamePlayer
    private String playerName = "Player";
    private String color = "red";

    @Test
    void addHealthBars() {
        player.setName(playerName);
        player.setColor(color);

        uT.setId(testId);
        uT.setType(type);
        uT.setHp(hp);
        uT.setMp(mp);
        uT.setCanAttack(canAttack);
        uT.setPosition(posiontion);
        uT.setGame(game);
        uT.setLeader(playerName);

        unitTiles.add(uT);
        System.out.println(unitTiles.get(0).getType());

        for (int j = 0; j < unitTiles.size(); j++) {
            Pane testPane = new Pane();
            unitPanes.add(testPane);

            testPane.getChildren().add(TextureManager.getTextureInstance(
                unitTiles.get(j).getType(), player.getColor()));
            testPane.getChildren().add(TextureManager.getTextureInstanceWithSize(
                "HealthBarBorder", 6, 50));
            testPane.getChildren().add(TextureManager.getTextureInstanceWithSize(
                "HealthBarBackground", 6, 50));
            testPane.getChildren().add(TextureManager.getTextureInstanceWithSize(
                "HealthBarForeground", 6, 50));

            assertEquals(testPane.getChildren().get(0), TextureManager.getTextureInstance(
                unitTiles.get(j).getType(), player.getColor()));
            assertEquals(testPane.getChildren().get(1), TextureManager.getTextureInstanceWithSize(
                "HealthBarBorder", 6, 50));
            assertEquals(testPane.getChildren().get(2), TextureManager.getTextureInstanceWithSize(
                "HealthBarBackground", 6, 50));
            assertEquals(testPane.getChildren().get(3), TextureManager.getTextureInstanceWithSize(
                "HealthBarForeground", 6, 50));

            for (int i = 1; i < testPane.getChildren().size(); i++) {
                testPane.getChildren().get(i).setLayoutY(55);
                assertEquals(testPane.getChildren().get(i).getLayoutY(), 55);
            }
        }
    }

    @Test
    void reduceHealth() {
        for (int i = 0; i < unitPanes.size(); i++) {
            if (unitTiles.get(i).getId().equals(testId)) {
                unitPanes.get(i).getChildren().remove(3);
                unitPanes.get(i).getChildren().add(TextureManager.getTextureInstanceWithSize(
                    "HealthBarForeground",
                    6, 3 * 5));
                unitPanes.get(i).getChildren().get(3).setLayoutY(55);
            }
        }

        assertEquals(unitPanes.get(0).getChildren().get(3), TextureManager.getTextureInstanceWithSize(
            "HealthBarForeground",
            6, 3 * 5));
    }
}
