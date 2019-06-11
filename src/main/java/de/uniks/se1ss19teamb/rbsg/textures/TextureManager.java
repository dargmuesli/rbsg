package de.uniks.se1ss19teamb.rbsg.textures;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.scene.layout.Pane;

public class TextureManager {
	
	private  static TextureManager instance = null;
	
	public static void init() {
		instance = new TextureManager();
		
		Texture test = new Texture("Test.png");
		instance.textures.put("test", test);
		
		Texture missing = new Texture("Missing.png");
		instance.textures.put("missing", missing);
	}
	
	public static Pane getTextureInstance(String toFetch) {
		return instance.fetchTexture(toFetch).instantiate();
	}
	
	public static Dimension2D getTextureDimensions(String toFetch) {
		Texture texture = instance.fetchTexture(toFetch);
		return new Dimension2D(texture.image.getWidth(), texture.image.getHeight());
	}
	
	private Map<String, Texture> textures = new HashMap<>();
	
	private TextureManager() { }
	
	private Texture fetchTexture(String toFetch) {
		Texture texture = textures.get(toFetch);
		
		if(texture == null) {
			texture = textures.get("missing");
		}
		
		return texture;
	}
}
