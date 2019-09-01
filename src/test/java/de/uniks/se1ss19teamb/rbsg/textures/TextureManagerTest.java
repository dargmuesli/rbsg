package de.uniks.se1ss19teamb.rbsg.textures;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class TextureManagerTest {

    @Start
    public void start(Stage stage) {
        TextureManager.init();
    }

    @Test
    void checkLoadedTexture() {
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("missing"));
        Assert.assertEquals(new Dimension2D(64, 256), TextureManager.getTextureDimensions("panzer"));

        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("bazooka"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("heavyTank"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("infantry"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("jeep"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("lightTank"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("helicopter"));

        Assert.assertEquals(new Dimension2D(32, 8), TextureManager.getTextureDimensions("HealthBarBorder"));
        Assert.assertEquals(new Dimension2D(32, 8), TextureManager.getTextureDimensions("HealthBarBackground"));
        Assert.assertEquals(new Dimension2D(32, 8), TextureManager.getTextureDimensions("HealthBarForeground"));
    }

    @Test
    void checkInstancedPanes() {
        Pane pane = TextureManager.getTextureInstance("test", null);
        Assert.assertFalse(pane.getChildren().filtered((node) -> node instanceof ImageView).isEmpty());
        Assert.assertNotNull(((ImageView) pane.getChildren()
            .stream().filter((node) -> node instanceof ImageView).findAny().get())
            .getImage());

        Pane animPane = TextureManager.getTextureInstance("panzer", "blue");
        Assert.assertTrue(animPane instanceof AnimatedPane);
        
        EnvironmentTile sand = new EnvironmentTile();
        sand.setId("Forest@23467");
        EnvironmentTile water = new EnvironmentTile();
        water.setId("Water@345678");
        EnvironmentTile mountain = new EnvironmentTile();
        mountain.setId("Mountain@456789");
        EnvironmentTile grass = new EnvironmentTile();
        grass.setId("Grass@12345");
        
        Map<Pair<Integer, Integer>, EnvironmentTile> map = new HashMap<>();
        map.put(new Pair<>(0,0), mountain);
        map.put(new Pair<>(0,1), mountain);
        map.put(new Pair<>(0,2), sand);
        
        map.put(new Pair<>(1,0), mountain);
        map.put(new Pair<>(1,1), water);
        map.put(new Pair<>(1,2), grass);
        
        map.put(new Pair<>(2,0), mountain);
        map.put(new Pair<>(2,1), grass);
        map.put(new Pair<>(2,2), sand);
        
        Pane fancyPane = TextureManager.computeTerrainTextureInstance(map, 1, 1);
        Assert.assertNotNull(fancyPane);
    }

    @Test
    void minimapTest() {
        //for terrain only
        Map<Pair<Integer, Integer>, EnvironmentTile> map = new HashMap<>();
        EnvironmentTile tile = new EnvironmentTile();
        tile.setId("Grass");
        map.put(new Pair<>(0, 0),tile);
        Canvas minimap = TextureManager.computeMinimap(map, -1, null);

        Assert.assertEquals(minimap.getGraphicsContext2D().getFill(), Paint.valueOf("Lime"));

        //for unit without player
        Map<String, UnitTile> unitMap = new HashMap<>();
        UnitTile unit = new UnitTile();
        unit.setPosition("Grass");
        unit.setLeader("player");
        unitMap.put("Grass", unit);
        Canvas minimapWithUnit = TextureManager.computeMinimap(map, -1, unitMap);

        Assert.assertEquals(minimapWithUnit.getGraphicsContext2D().getFill(), Paint.valueOf("black"));

        InGamePlayer player = new InGamePlayer();
        player.setColor("red");
        InGameController.inGameObjects.put("player", player);

        Canvas minimapWithUnitwithPlayer = TextureManager.computeMinimap(map, -1, unitMap);

        Assert.assertEquals(minimapWithUnitwithPlayer.getGraphicsContext2D().getFill(), Paint.valueOf("red"));

        InGameController.inGameObjects.clear();
    }
}
