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
     * Kaiten Strategy (KISS):
     * 
     * Calculate Average Army Coordinates for each available type of Unit.
     * Pick Closest Attackable Enemy for each Type
     * Move Towards it.
     * If next to it: Attack.
     * 
     */
    
    public Kaiten() {
    }

    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    @SuppressWarnings ("static-access")
    @Override
    protected void doTurnInternal() {
        
        Map<UnitTile, UnitTile> markedForAttack = new HashMap<>();
        
        //For each type of Unit
        for (Unit unitType : availableUnitTypes.values()) {
            
            int centerX = 0;
            int centerY = 0;
            
            int cnt = 0;
            
            //Interate over friendlies to calculate average
            for (UnitTile unit : ingameController.unitTiles) {
                
                //If not friendly, skip
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
            
            //Interate over enemies to find closest
            for (UnitTile unit : ingameController.unitTiles) {
                
                //If not enemy, skip
                if (unit.getLeader().equals(playerID)) {
                    continue;
                }
                
                //If cannot be attacked, skip
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
            
            //Iterate over Friendlies to Move towards enemy
            for (UnitTile unit : ingameController.unitTiles) {
                
                //If not friendly or wrong type, skip
                if (!unit.getLeader().equals(playerID) || !unit.getType().equals(unitType.getType())) {
                    continue;
                }
                
                Pair<Path, Integer> path = findClosestAccessibleField(unit, toAttack.getX(), toAttack.getY(), false);
                
                socket.moveUnit(unit.getId(), path.getKey().path);
                
                //If we land next to the Target, mark for Attacking
                if (path.getValue() == 1) {
                    markedForAttack.put(unit, ingameController.unitTileMapByTileId.get(toAttack.getId()));
                }
                
                waitForSocket();
            }
            
        }
        
        //Move To Attack Phase
        socket.nextPhase();
        waitForSocket();
        
        for (Entry<UnitTile, UnitTile> attack : markedForAttack.entrySet()) {
            socket.attackUnit(attack.getKey().getId(), attack.getValue().getId());
            waitForSocket();
        }
        
        //Move To 2nd Move Phase
        socket.nextPhase();
        waitForSocket();
                
                
        //End Turn
        socket.nextPhase();
        waitForSocket();
        
    }

    @Override
    public List<String> requestArmy() {
        return null;
    }
    
}
