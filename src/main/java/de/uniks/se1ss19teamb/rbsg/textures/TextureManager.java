package de.uniks.se1ss19teamb.rbsg.textures;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class TextureManager {

    private static TextureManager instance = null;
    private Map<String, Texture> textures = new HashMap<>();
    private Map<String, TextureFancy> texturesTerrain = new HashMap<>();
    private Map<String, Color> terrainColors = new HashMap<>();

    private TextureManager() {
    }

    public static void init() {
        instance = new TextureManager();
        AnimatedTexture.registerAnimUpdates();

        Texture panzer = new AnimatedTexture("panzer.png", 250.0f);
        instance.textures.put("panzer", panzer);

        Texture heli = new AnimatedTexture("HelicopterV1anim.png", 125.0f);
        instance.textures.put("Chopper", heli);

        Texture bazooka = new Texture("BazookaV1.png");
        instance.textures.put("Bazooka Trooper", bazooka);

        Texture heavyTank = new Texture("HeavyTankV1.png");
        instance.textures.put("Heavy Tank", heavyTank);

        Texture infantry = new Texture("InfantryV1.png");
        instance.textures.put("Infantry", infantry);

        Texture jeep = new Texture("JeepV1.png");
        instance.textures.put("Jeep", jeep);

        Texture lightTank = new Texture("lightTankV1.png");
        instance.textures.put("Light Tank", lightTank);

        Texture healthBarBorder = new Texture("healthBorder.png");
        instance.textures.put("HealthBarBorder", healthBarBorder);

        Texture healthBarBackground = new Texture(("healthBack.png"));
        instance.textures.put("HealthBarBackground", healthBarBackground);

        Texture healthBarForeground = new Texture("healthFore.png");
        instance.textures.put("HealthBarForeground", healthBarForeground);

        Texture missing = new Texture("Missing.png");
        instance.textures.put("missing", missing);
        
        
        TextureFancy water = new TextureFancy("water.png", "water.png", 0);
        instance.texturesTerrain.put("Water", water);
        instance.terrainColors.put("Water", Color.CYAN);
        
        TextureFancy sand = new TextureFancy("grass.png", "grassOverlay.png", 1);
        instance.texturesTerrain.put("Grass", sand);
        instance.terrainColors.put("Grass", Color.LIME);
        
        TextureFancy grass = new TextureFancy("forest.png", "grassOverlay.png", 2);
        instance.texturesTerrain.put("Forest", grass);
        instance.terrainColors.put("Forest", Color.SEAGREEN);
        
        TextureFancy mountain = new TextureFancy("mountain.png", "mountainOverlay.png", 3);
        instance.texturesTerrain.put("Mountain", mountain);
        instance.terrainColors.put("Mountain", Color.SLATEGREY);
    }

    public static Pane getTextureInstance(String toFetch, String color) {
        return instance.fetchTexture(toFetch).instantiate(color);
    }

    public static Pane getTextureInstanceWithSize(String toFetch, double heigth, double width) {
        return instance.fetchTexture(toFetch).instantiate(heigth, width);
    }
    
    public static Canvas computeMinimap(
        Map<Pair<Integer, Integer>, EnvironmentTile> map, double size, Map<String, UnitTile> unitTileMapByTileId) {

        // since it's a square map
        double squareSide = Math.sqrt(map.size());

        if (size < 0) {
            // 3 times big map side length
            size = 192 / squareSide;
        }

        Canvas canvas = new Canvas(squareSide * size, squareSide * size);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        for (Entry<Pair<Integer, Integer>, EnvironmentTile> tile : map.entrySet()) {
            Pair<Integer, Integer> pos = tile.getKey();

            UnitTile possibleUnit = unitTileMapByTileId.get(tile.getValue().getId());

            if (possibleUnit != null) {
                InGamePlayer player = (InGamePlayer) InGameController.inGameObjects.get(possibleUnit.getLeader());
                if (player != null) {
                    gc.setFill(Color.valueOf(player.getColor()));
                } else {
                    gc.setFill(Color.BLACK);
                }

            } else {
                gc.setFill(instance.terrainColors.get(tile.getValue().getName()));
            }

            gc.fillRect(pos.getKey() * size, pos.getValue() * size, size, size);
        }

        return canvas;
    }
    
    public static Pane computeTerrainTextureInstance(Map<Pair<Integer, Integer>, EnvironmentTile> map, int x, int y) {
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
                
                EnvironmentTile horizontal = map.get(new Pair<>(x + pos.x, y));
                EnvironmentTile vertical = map.get(new Pair<>(x, y + pos.y));
                EnvironmentTile diagonal = map.get(new Pair<>(x + pos.x, y + pos.y));
                
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
