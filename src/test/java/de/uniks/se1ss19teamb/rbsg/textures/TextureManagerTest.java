package de.uniks.se1ss19teamb.rbsg.textures;

import de.uniks.se1ss19teamb.rbsg.model.InGameTile;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextureManagerTest {

    @Before
    public void prepareTest() {
        TextureManager.init();
    }

    @Test
    public void checkLoadedTexture() {
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("missing"));
        Assert.assertEquals(new Dimension2D(64, 256), TextureManager.getTextureDimensions("panzer"));

        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("bazooka"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("heavyTank"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("infantry"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("jeep"));
        Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("lightTank"));
        Assert.assertEquals(new Dimension2D(64, 192), TextureManager.getTextureDimensions("helicopter"));
    }

    @Test
    public void checkInstancedPanes() {
        Pane pane = TextureManager.getTextureInstance("test");
        Assert.assertFalse(pane.getChildren().filtered((node) -> node instanceof ImageView).isEmpty());
        Assert.assertFalse(((ImageView) pane.getChildren()
            .stream().filter((node) -> node instanceof ImageView).findAny().get())
            .getImage() == null);

        Pane animPane = TextureManager.getTextureInstance("panzer");
        Assert.assertTrue(animPane instanceof AnimatedPane);
        
        InGameTile sand = new InGameTile();
        sand.setId("sand");
        InGameTile water = new InGameTile();
        water.setId("water");
        InGameTile mountain = new InGameTile();
        mountain.setId("mountain");
        InGameTile grass = new InGameTile();
        grass.setId("grass");
        
        Map<Pair<Integer, Integer>, InGameTile> map = new HashMap<>();
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
