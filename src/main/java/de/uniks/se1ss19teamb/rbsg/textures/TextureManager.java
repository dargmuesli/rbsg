package de.uniks.se1ss19teamb.rbsg.textures;

import de.uniks.se1ss19teamb.rbsg.model.InGameTile;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import javafx.geometry.Dimension2D;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;

public class TextureManager {

    private static TextureManager instance = null;
    private Map<String, Texture> textures = new HashMap<>();
    private Map<String, TextureFancy> texturesTerrain = new HashMap<>();

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
        
        
        TextureFancy water = new TextureFancy("water.png", "water.png", 0);
        instance.texturesTerrain.put("Water", water);
        
        TextureFancy sand = new TextureFancy("grass.png", "grassOverlay.png", 1);
        instance.texturesTerrain.put("Grass", sand);
        
        TextureFancy grass = new TextureFancy("forest.png", "grassOverlay.png", 2);
        instance.texturesTerrain.put("Forest", grass);
        
        TextureFancy mountain = new TextureFancy("mountain.png", "mountainOverlay.png", 3);
        instance.texturesTerrain.put("Mountain", mountain);
    }

    public static Pane getTextureInstance(String toFetch) {
        return instance.fetchTexture(toFetch).instantiate();
    }

    public static Pane computeTerrainTextureInstance(Object map, int x, int y){
        return getTextureInstance("missing");
    }
    
    public static Pane computeTerrainTextureInstance(Map<Pair<Integer, Integer>, InGameTile> map, int x, int y) {
        TextureFancy current = instance.texturesTerrain.get(map.get(new Pair<>(x, y)).getName());
        
        GridPane overlay = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(32);
        ColumnConstraints column2 = new ColumnConstraints(32);
        RowConstraints row1 = new RowConstraints(32);
        RowConstraints row2 = new RowConstraints(32);
        overlay.getColumnConstraints().addAll(column1, column2);
        overlay.getRowConstraints().addAll(row1, row2);
        
        for (TextureFancyOverlayPosition pos : TextureFancyOverlayPosition.values()) { 
            NavigableMap<Integer, Pane> currentOverlayPane = new TreeMap<>();
            
            for (Entry<String,TextureFancy> texture : instance.texturesTerrain.entrySet()) {
                if (texture.getValue().getDepth() <= current.getDepth()) {
                    continue;
                }
                
                TextureFancyOverlayType type = null;
                
                InGameTile horizontal = map.get(new Pair<>(x + pos.x, y));
                InGameTile vertical = map.get(new Pair<>(x, y + pos.y));
                InGameTile diagonal = map.get(new Pair<>(x + pos.x, y + pos.y));
                
                if (horizontal != null && vertical != null 
                    && horizontal.getName().equals(texture.getKey()) && vertical.getName().equals(texture.getKey())) {
                    type = TextureFancyOverlayType.BOTH;
                } else if (horizontal != null && horizontal.getName().equals(texture.getKey())) {
                    type = TextureFancyOverlayType.HORIZONTAL;
                } else if (vertical != null && vertical.getName().equals(texture.getKey())) {
                    type = TextureFancyOverlayType.VERTICAL;
                } else if (diagonal != null && diagonal.getName().equals(texture.getKey())) {
                    type = TextureFancyOverlayType.DIAGONAL;
                }
                
                //Current Terrain OVerlay not found in Neighbours
                if (type == null) {
                    continue;
                }
                    
                currentOverlayPane.put(texture.getValue().getDepth(), texture.getValue().instantiateOverlay(pos, type));
            }
            
            if (currentOverlayPane.isEmpty()) {
                continue;
            }
                
            Pane parent = currentOverlayPane.firstEntry().getValue();
            overlay.add(parent, (pos.x + 1) / 2, (pos.y + 1) / 2);
            
            while (currentOverlayPane.size() > 1) {
                currentOverlayPane = currentOverlayPane.tailMap(currentOverlayPane.firstKey(), false);
                parent.getChildren().add(currentOverlayPane.firstEntry().getValue());
                parent = currentOverlayPane.firstEntry().getValue();
            }
        }

        Pane base = current.instantiateBase();
        
        base.getChildren().add(overlay);
        
        return base;
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
