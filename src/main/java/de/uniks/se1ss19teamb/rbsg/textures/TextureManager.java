package de.uniks.se1ss19teamb.rbsg.textures;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.scene.layout.Pane;

public class TextureManager {

    private static TextureManager instance = null;
    private Map<String, Texture> textures = new HashMap<>();

    private TextureManager() {
    }

    public static void init() {
        instance = new TextureManager();
        AnimatedTexture.registerAnimUpdates();

        Texture panzer = new AnimatedTexture("panzer.png", 250.0f);
        instance.textures.put("panzer", panzer);

        Texture heli = new AnimatedTexture("HelicopterV1anim.png", 125.0f);
        instance.textures.put("helicopter", heli);

        Texture bazooka = new Texture("BazookaV1.png");
        instance.textures.put("bazooka", bazooka);

        Texture heavyTank = new Texture("HeavyTankV1.png");
        instance.textures.put("heavyTank", heavyTank);

        Texture infantry = new Texture("InfantryV1.png");
        instance.textures.put("infantry", infantry);

        Texture jeep = new Texture("JeepV1.png");
        instance.textures.put("jeep", jeep);

        Texture lightTank = new Texture("lightTankV1.png");
        instance.textures.put("lightTank", lightTank);

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

    private Texture fetchTexture(String toFetch) {
        Texture texture = textures.get(toFetch);

        if (texture == null) {
            texture = textures.get("missing");
        }

        return texture;
    }
}
