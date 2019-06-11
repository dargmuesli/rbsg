package de.uniks.se1ss19teamb.rbsg.textures;

import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Dimension2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import org.junit.Assert;

public class TextureManagerTest {
	
	@Before
	public void prepareTest() {
		TextureManager.init();
	}
	
	@Test
	public void checkLoadedTexture() {
		Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("test"));
		Assert.assertEquals(new Dimension2D(64, 64), TextureManager.getTextureDimensions("missing"));
	}
	
	@Test
	public void checkInstancedPanes() {
		Pane pane = TextureManager.getTextureInstance("test");
		Assert.assertFalse(pane.getChildren().filtered((node) -> node instanceof ImageView).isEmpty());
		Assert.assertFalse(((ImageView)pane.getChildren()
				.stream().filter((node) -> node instanceof ImageView).findAny().get())
				.getImage() == null);
	}
}
