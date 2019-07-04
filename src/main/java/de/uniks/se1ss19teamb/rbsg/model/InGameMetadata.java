package de.uniks.se1ss19teamb.rbsg.model;

public class InGameMetadata {
    private String id;
    private String[] allPlayer;
    private String[] allUnits;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id: ").append(id).append("\nallPlayer: ");
        for (String player : allPlayer) {
            stringBuilder.append(player);
        }
        stringBuilder.append("\nAllUnits: ");
        for (String unit : allUnits) {
            stringBuilder.append(unit);
        }
        return stringBuilder.toString();
    }
}
