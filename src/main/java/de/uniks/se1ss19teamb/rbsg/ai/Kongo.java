package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.SortedSet;
import java.util.TreeSet;

import javafx.util.Pair;


class Kongo extends AI {
    
    protected Map<String, String> toAttack = new HashMap<>(); //Key: Own, Value: Enemy
    protected Map<String, List<String>> prioritize = new HashMap<>();
    protected Map<String, List<String>> ignore = new HashMap<>();
    
    /*
     * Kongo Strategy:
     * 
     * Move Choppers, attacking Bazooka > Light Tank > Infantry > Jeep
     * while keeping Clear of Heavy Tanks
     * 
     * Heavy Tanks: Heavy Tank > Chopper > Light Tank > Bazooka > Infantry > Jeep
     * 
     * Light Tanks: Jeep > Infantry > Bazooka > Light Tank
     * While keeping Clear of heavy Tanks & Choppers
     * 
     * Jeeps: Bazooka > Jeep > Infantry
     * While keeping Clear of Light Tanks & heavy Tanks & Choppers
     * 
     * Bazooka: Chopper > Jeep
     * While Keeping clear of Infantry & Light Tanks & heavy Tanks
     * 
     * Infantry: Bazooka > Infantry
     * While keeping Clear of Light Tanks & heavy Tanks & Choppers & Jeeps
     * 
     */
    
    
    
    public Kongo() {
        prioritize.put("Heavy Tank", Arrays.asList(new String[] {
            "Heavy Tank", "Chopper", "Light Tank", "Bazooka Trooper", "Infantry", "Jeep"
        }));
        
        ignore.put("Heavy Tank", Arrays.asList(new String[] {
        }));
        
        prioritize.put("Chopper", Arrays.asList(new String[] {
            "Bazooka Trooper", "Light Tank", "Infantry", "Jeep"
        }));
        
        ignore.put("Chopper", Arrays.asList(new String[] {
            "Heavy Tank"
        }));
        
        prioritize.put("Light Tank", Arrays.asList(new String[] {
            "Jeep", "Infantry", "Bazooka Trooper", "Light Tank"
        }));
        
        ignore.put("Light Tank", Arrays.asList(new String[] {
            "Heavy Tank", "Chopper"
        }));
        
        prioritize.put("Jeep", Arrays.asList(new String[] {
            "Bazooka Trooper", "Jepp", "Infantry"
        }));
        
        ignore.put("Jeep", Arrays.asList(new String[] {
            "Heavy Tank", "Chopper", "Light Tank"
        }));
        
        prioritize.put("Bazooka Trooper", Arrays.asList(new String[] {
            "Chopper", "Jeep"
        }));
        
        ignore.put("Bazooka Trooper", Arrays.asList(new String[] {
            "Heavy Tank", "Chopper", "Light Tank"
        }));
        
        prioritize.put("Infantry", Arrays.asList(new String[] {
            "Bazooka Trooper", "Infantry"
        }));
        
        ignore.put("Infantry", Arrays.asList(new String[] {
            "Heavy Tank", "Chopper", "Light Tank", "Jeep"
        }));
    }
    
    @Override
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
        super.initialize(playerID, socket, controller);
    }

    @SuppressWarnings ("static-access")
    @Override
    protected void doTurnInternal() {
        toAttack.clear();
        for (UnitTile tile : ingameController.unitTiles) {
            tile.setMpLeft(tile.getMp());
        }
        
        //Move Helicopter Strike Force
        movement();
        
        //Move To Attack Phase
        socket.nextPhase();
        waitForSocket();
        
        //TODO Tank Movement Needed? For now, Concede
        if (socket.phaseString.equals("Movement Phase")) {
            //FIXME Does not yet work, somehow
            //For now: Manual recommendation
            NotificationHandler.sendInfo("AI wants to concede", logger);
            //socket.leaveGame();
            //socket.disconnect();
            return;
        }
        
        attackAvailable();
        
        //Move To 2nd Movement Phase
        socket.nextPhase();
        waitForSocket();
        
        //End Turn
        socket.nextPhase();
        waitForSocket();      
    }

    protected void attackAvailable() {
        for (Entry<String, String> attack : toAttack.entrySet()) {
            socket.attackUnit(attack.getKey(), attack.getValue());
            waitForSocket();
        }
    }

    @SuppressWarnings ("static-access")
    protected void movement() {
        
        for (UnitTile unit : ingameController.unitTiles) {
            //Iterate over own Units
            if (!unit.getLeader().equals(playerID)) {
                continue;
            }
            
            ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                    unit.getPosition()), unit.getMpLeft(), false,
                    ((InGamePlayer)ingameController.inGameObjects
                    .get(playerID)).getName());
            
            Map<String, String> previousTileMapByIdLocal = new HashMap<>(ingameController.previousTileMapById);
            if (previousTileMapByIdLocal.isEmpty()) {
                continue;
            }
            List<String> possibleDestinations = new ArrayList<>(previousTileMapByIdLocal.keySet());
            
            for (UnitTile enemyUnit : ingameController.unitTiles) {
                //Iterate over enemy units to ignore
                
                if (enemyUnit.getLeader().equals(playerID) 
                        || !ignore.get(unit.getType()).contains(enemyUnit.getType())) {
                    continue;
                }
                
                ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                        enemyUnit.getPosition()), enemyUnit.getMp() + 1, false,
                        ((InGamePlayer)ingameController.inGameObjects
                        .get(enemyUnit.getLeader())).getName());
                
                possibleDestinations.removeAll(ingameController.previousTileMapById.keySet());
            }
            
            if (possibleDestinations.isEmpty()) {
                //TODO Unit can't move outside of HT Attack Range. FInd Better solution
                possibleDestinations.addAll(previousTileMapByIdLocal.keySet());
            }
            
            List<String> prio = prioritize.get(unit.getType());
            
            SortedSet<UnitTile> targets = new TreeSet<>((unitL, unitR) -> {
                int indexL = prio.indexOf(unitL.getType());
                int indexR = prio.indexOf(unitR.getType());
                
                if (indexL == indexR) {
                    EnvironmentTile unitTile = ingameController.environmentTileMapById.get(unit.getPosition());
                    EnvironmentTile tileL = ingameController.environmentTileMapById.get(unitL.getPosition());
                    EnvironmentTile tileR = ingameController.environmentTileMapById.get(unitR.getPosition());
                    
                    int distL = Math.abs(tileL.getX() - unitTile.getX()) + Math.abs(tileL.getY() - unitTile.getY());
                    int distR = Math.abs(tileR.getX() - unitTile.getX()) + Math.abs(tileR.getY() - unitTile.getY());
                    
                    return distL - distR;
                } else if (indexL == -1) {
                    return 1000;
                } else if (indexR == -1) {
                    return -1000;
                } else {
                    return (indexL - indexR) * (64 + 1);
                }
            });
            
            for (UnitTile enemy : ingameController.unitTiles) {
                //Iterate over enemys that are attackable
                if (enemy.getLeader().equals(playerID)
                        || !Arrays.asList(unit.getCanAttack()).contains(enemy.getType())) {
                    continue;
                }
                
                targets.add(enemy);
            }
            
            if (targets.isEmpty()) {
                //idle to move at least one unit per turn
                EnvironmentTile unitTile = ingameController.environmentTileMapById.get(unit.getPosition());
                Pair<Path, Integer> path = findClosestAccessibleField(unit, unitTile.getX(), unitTile.getY(), false);
                socket.moveUnit(unit.getId(), path.getKey().path);
                waitForSocket();
            }
            
            UnitTile target = targets.first();
            EnvironmentTile targetUnitTile = ingameController.environmentTileMapById.get(target.getPosition());
            
            String closest = null;
            int closestDistance = Integer.MAX_VALUE;
            
            for (String targetTile : possibleDestinations) {
                EnvironmentTile current = ingameController.environmentTileMapById.get(targetTile);
                
                int currentDistance = Math.abs(targetUnitTile.getX() - current.getX()) 
                        + Math.abs(targetUnitTile.getY() - current.getY());
                
                //Forbid walking onto the target
                if (currentDistance < closestDistance && currentDistance > 0) {
                    closestDistance = currentDistance;
                    closest = current.getId();
                }
            }
            
            Path path = new Path();
            path.end = ingameController.environmentTileMapById.get(closest);
            path.start = ingameController.environmentTileMapById.get(unit.getPosition());        
            path.distance = 0;
            
            LinkedList<String> pathList = new LinkedList<>();

            do {
                path.distance++;
                pathList.addFirst(closest);
                closest = previousTileMapByIdLocal.get(closest);
            } while (!closest.equals(unit.getPosition()));

            path.path = pathList.toArray(new String[0]);
            
            socket.moveUnit(unit.getId(), path.path);
            waitForSocket();
        
            //If we land next to the Target, mark for Attacking
            if (closestDistance == 1) {
                toAttack.put(unit.getId(), target.getId());
            }
        }        
    }

    @Override
    public List<String> requestArmy() {
        return null;
    }
}