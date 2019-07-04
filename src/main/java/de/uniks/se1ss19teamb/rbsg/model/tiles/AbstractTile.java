package de.uniks.se1ss19teamb.rbsg.model.tiles;

public abstract class AbstractTile {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return id.replaceFirst("@.+", "");
    }
}
