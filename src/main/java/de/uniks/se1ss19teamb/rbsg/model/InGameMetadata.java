package de.uniks.se1ss19teamb.rbsg.model;

public class InGameMetadata {
    private String id;
    private String[] allPlayer;
    private String[] allUnits;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id: ").append(id).append("\nallPlayer: ");
        for (String player : allPlayer) {
            stringBuilder.append("\"").append(player).append("\" ");
        }
        stringBuilder.append("\nAllUnits: ");
        for (String unit : allUnits) {
            stringBuilder.append("\"").append(unit).append("\" ");
        }
        return stringBuilder.toString();
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setAllPlayer(String[] allPlayer) {
        this.allPlayer = allPlayer;
    }

    public void setAllUnits(String[] allUnits) {
        this.allUnits = allUnits;
    }
}
