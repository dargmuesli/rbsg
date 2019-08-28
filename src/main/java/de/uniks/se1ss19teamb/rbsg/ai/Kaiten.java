package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.util.Pair;

class Kaiten extends AI {

    /*
     * Kaiten strategy (KISS):
     *
     * Calculate average army coordinates for each available unit type.
     * Pick closest attackable enemy for each type.
     * Move towards it.
     * If next to it, attack.
     */

    public Kaiten() {
    }

    /*
     * Suppress warning, because in the near future relevant fields can't be statically accessed anymore.
     */
    @SuppressWarnings("static-access")
    @Override
    protected void doTurnInternal() {
        for (UnitTile tile : ingameController.unitTiles) {
            tile.setMpLeft(tile.getMp());
        }


        Map<UnitTile, UnitTile> markedForAttack = new HashMap<>();

        // Iterate over unit types.
        for (Unit unitType : availableUnitTypes.values()) {

            int centerX = 0;
            int centerY = 0;

            int cnt = 0;

            // Iterate over friendly units to calculate average position.
            for (UnitTile unit : ingameController.unitTiles) {

                // If not friendly, skip.
                if (!unit.getLeader().equals(playerID)) {
                    continue;
                }

                EnvironmentTile tile = ingameController.environmentTileMapById.get(unit.getPosition());

                centerX += tile.getX();
                centerY += tile.getY();

                cnt++;
            }

            centerX /= cnt;
            centerY /= cnt;

            int closestDistance = Integer.MAX_VALUE;
            EnvironmentTile toAttack = null;

            // Iterate over enemies to find the closest.
            for (UnitTile unit : ingameController.unitTiles) {

                // If not enemy, skip.
                if (unit.getLeader().equals(playerID)) {
                    continue;
                }

                // If attack is impossible, skip.
                if (!unitType.getCanAttack().contains(unit.getType())) {
                    continue;
                }

                EnvironmentTile tile = ingameController.environmentTileMapById.get(unit.getPosition());

                int currentDistance = Math.abs(centerX - tile.getX()) + Math.abs(centerY - tile.getY());

                if (currentDistance < closestDistance) {
                    closestDistance = currentDistance;
                    toAttack = tile;
                }
            }

            if (toAttack == null) {
                continue;
            }

            // Iterate over friendlies to move towards the enemy.
            for (UnitTile unit : ingameController.unitTiles) {

                // If not friendly or type is wrong, skip.
                if (!unit.getLeader().equals(playerID) || !unit.getType().equals(unitType.getType())) {
                    continue;
                }

                assert toAttack != null;
                Pair<Path, Integer> path = findClosestAccessibleField(unit, toAttack.getX(), toAttack.getY(), false);

                if (path != null) {
                    socket.moveUnit(unit.getId(), path.getKey().path);

                    // If we land next to the target, mark it for attack.
                    if (path.getValue() == 1) {
                        markedForAttack.put(unit, ingameController.unitTileMapByTileId.get(toAttack.getId()));
                    }
                }

                waitForSocket();
            }

        }

        // Move to attack phase.
        socket.nextPhase();
        waitForSocket();

        for (Entry<UnitTile, UnitTile> attack : markedForAttack.entrySet()) {
            socket.attackUnit(attack.getKey().getId(), attack.getValue().getId());
            waitForSocket();
        }

        // Move to 2nd move phase.
        socket.nextPhase();
        waitForSocket();


        // End turn.
        socket.nextPhase();
        waitForSocket();

    }

    @Override
    public List<String> requestArmy() {
        return null;
    }

}
