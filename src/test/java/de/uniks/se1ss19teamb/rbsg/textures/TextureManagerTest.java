package de.uniks.se1ss19teamb.rbsg.textures;

import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    }

    @Test
    void checkInstancedPanes() {
        Pane pane = TextureManager.getTextureInstance("test", null);
        Assert.assertFalse(pane.getChildren().filtered((node) -> node instanceof ImageView).isEmpty());
        Assert.assertNotNull(((ImageView) pane.getChildren()
            .stream().filter((node) -> node instanceof ImageView).findAny().get())
            .getImage());

        Pane animPane = TextureManager.getTextureInstance("panzer", null);
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
}
