package de.uniks.se1ss19teamb.rbsg.textures;

import javafx.geometry.Dimension2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
        Assert.assertFalse(((ImageView)pane.getChildren()
                .stream().filter((node) -> node instanceof ImageView).findAny().get())
                .getImage() == null);
        
        Pane animPane = TextureManager.getTextureInstance("panzer");
        Assert.assertTrue(animPane instanceof AnimatedPane);
    }
}
