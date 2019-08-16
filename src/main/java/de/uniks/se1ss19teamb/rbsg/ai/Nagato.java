package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.util.Pair;


class Nagato extends AI {
    
    private static final int AMOUNT_HEAVY_TANKS = 7;

    private Map<String, String> tankPositions = new HashMap<>();
    
    private Map<UnitTile, Integer> projectedHP = new HashMap<>();
    private Map<String, String> toAttack = new HashMap<>(); //Key: Own, Value: Enemy
    
    /*
     * Nagato Strategy:
     * 
     * Pick 6 - 8 Heavy Tanks, 4 - 2 Choppers
     * 
     * Position Tanks at a forest -> Grass Border (if none in friendly half of map, mountain -> forest)
     * 
     * Move Choppers as Mobile Strike force, attacking Bazooka > Light Tank > Infantry > Jeep
     * while keeping Clear of Heavy Tanks
     * 
     * Heavy Tanks:
     * If Enemy in Attack Range/2 of HT -> Attack closest and retreat
     * If Enemy downable with In-Range Tanks and no other enemy HT in Range, attack and retreat next round
     * Focus Order: Heavy Tank > Chopper > Light Tank > Bazooka > Infantry > Jeep
     * 
     */
    
    public Nagato() {
    }
    
    @SuppressWarnings ("static-access")
    @Override
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
        super.initialize(playerID, socket, controller);
        
        final int sideLength = (int) Math.sqrt(controller.environmentTiles.size());
        //Calculate HT Position
        
        boolean sideDirLow = false; //True if starting at 0 on relevant side
        boolean sideXY = false; //True if relevant side is on edge of x axis
        
        //Figure out which side of the Board we are on
        {
            EnvironmentTile randomFriendly = null;
        
            for (UnitTile unit : controller.unitTiles) {
                if (unit.getLeader().equals(playerID)) {
                    randomFriendly = controller.environmentTileMapById.get(unit.getPosition());
                    break;
                }
            }
            
            float quot = ((float) Math.abs(randomFriendly.getX() - sideLength / 2))
                    / ((float) Math.abs(randomFriendly.getY() - sideLength / 2));
            
            sideXY = (quot > 1);
            
            sideDirLow = (sideXY ? randomFriendly.getX() : randomFriendly.getY()) > sideLength / 2;
        }
        
        //Iterate over our Quarter of the Field, and "normalize" it to our direction
        //if Four Players, take Quarter, if two, take half
        int startShort = sideDirLow ? sideLength - 1 : 0;
        int stopShort = sideLength / 2 - (sideDirLow ? 1 : 0);
        int lineCnt = 0;
        int players = (int) controller.inGameObjects.values().stream().filter(p -> p instanceof InGamePlayer).count();
        
        SortedMap<Pair<Integer, Integer>, EnvironmentTile> mapQuarter = new TreeMap<>(new PairComperatorXY(sideLength));
        
        for (int shortSide = startShort; shortSide != stopShort; shortSide -= (sideDirLow ? 1 : -1)) {
            for (int longSide = (players != 2 ? lineCnt : 0);
                    longSide < sideLength - (players != 2 ? lineCnt : 0); longSide++) {
                mapQuarter.put(new Pair<Integer, Integer>(longSide, lineCnt), controller.environmentTiles.get(
                        new Pair<Integer, Integer>(sideXY ? shortSide : longSide, sideXY ? longSide : shortSide)));
            }
            lineCnt++;
            
        }
        
        SortedMap<Integer, Integer> forestEdges = new TreeMap<>();
        
        //Now that we have a normalized map, let's walk through it
        for (Entry<Pair<Integer, Integer>, EnvironmentTile> tile : mapQuarter.entrySet()) {
            EnvironmentTile above = mapQuarter.get(new Pair<>(tile.getKey().getKey(), tile.getKey().getValue() + 1));
            
            //Skip those that are not a Forest/Mountain Edge
            if (above == null || tile.getValue().getName().equals("Grass")
                    || tile.getValue().getName().equals("Water")) {
                continue;
            }
            
            //Found a Forest/Mountain to Grass Edge
            if (above.getName().equals("Grass")) {
                forestEdges.put(tile.getKey().getKey(), tile.getKey().getValue());
            }
            
        }
        
        //Fill up if not enough good positions
        for (int i = forestEdges.size(); i < AMOUNT_HEAVY_TANKS; i++) {
            //TODO Better Alt. Positions with bad Map
            Random r = new Random();
            int x = 0;
            int y = 0;
            do {
                x = r.nextInt(sideLength);
                y = r.nextInt(sideLength / 2);
            } while (mapQuarter.get(new Pair<>(x, y)) == null || forestEdges.containsKey(x));
            
            forestEdges.put(x, y);
        }
        
        //Pick Middle Elements
        //TODO Improve to "Biggest Block"
        int skip = (forestEdges.size() - AMOUNT_HEAVY_TANKS) / 2;
        Iterator<Entry<Integer, Integer>> it = forestEdges.entrySet().iterator(); 
        for (int i = 0; i < skip; i++) {
            it.next();
        }

        int unitCnt = -1;
        
        //Assign each Tank a position
        for (int i = 0; i < AMOUNT_HEAVY_TANKS; i++) {
            Entry<Integer, Integer> position = it.next();
            
            UnitTile unit = null;
            
            do {
                unitCnt++;
                unit = controller.unitTiles.get(unitCnt);
            } while (!(unit.getLeader().equals(playerID) && unit.getType().equals("Heavy Tank")));
             
            tankPositions.put(unit.getId(), mapQuarter.get(new Pair<>(position.getKey(), position.getValue())).getId());
        }
    }

    @Override
    protected void doTurnInternal() {
        //Move Tanks to Forest Edge
        tankReposition();
        //Move Tanks to Attack if they have enough MP to return
        tankMovementAttackSafe();
        //Determine if closest Enemy can be killed with available Force and potentially reenforce attack
        tankMovementAttackAdditional();
        
        //Move Helicopter Strike Force
        helicopterMovement();
        
        //Move To Attack Phase
        socket.nextPhase();
        waitForSocket();
        
        //TODO Tank Movement Needed? For now, Concede
        if (socket.phaseString.equals("Movement Phase")) {
            //FIXME Does not yet work, somehow
            //For now: Manual recommendation
            NotificationHandler.getInstance().sendInfo("AI wants to concede", logger);
            //socket.leaveGame();
            //socket.disconnect();
            return;
        }
        
        attackAvailable();
        
        //Move To 2nd Movement Phase
        socket.nextPhase();
        waitForSocket();
        
        tankRetreat();
        
        //End Turn
        socket.nextPhase();
        waitForSocket();      
    }

    private void attackAvailable() {
        // TODO Auto-generated method stub
        
    }

    private void helicopterMovement() {
        // TODO Auto-generated method stub
        
    }

    private void tankRetreat() {
        // TODO Auto-generated method stub
        
    }
    
    private void tankMovementAttackAdditional() {
        // TODO Auto-generated method stub
        
    }

    @SuppressWarnings ("static-access")
	private void tankMovementAttackSafe() {
    	for (Entry<String, String> position : tankPositions.entrySet()) {
            
            //Skip repositioning Dead Units
            UnitTile tank = null;
            for (UnitTile tiles : ingameController.unitTiles) {
                if (tiles.getId().equals(position.getKey())) {
                    tank = tiles;
                }
            }

            if (tank == null) {
                continue;
            }
            
            TreeMap<Path, UnitTile> attackable = findAllAttackableEnemies(tank);
            
            if (attackable.firstKey().distance <= tank.getMp() / 2) {
            	socket.moveUnit(tank.getId(), attackable.firstKey().path);  
            	waitForSocket();
            	flagForAttackByHeavyTank(attackable.firstEntry().getValue(), tank);
            }
    	}
        
    }
    
    @SuppressWarnings ("static-access")
	private void flagForAttackByHeavyTank(UnitTile target, UnitTile by) {
    	toAttack.put(by.getId(), target.getId());
    	int hp = projectedHP.get(target);
    	
    	EnvironmentTile field = ingameController.environmentTileMapById.get(target.getPosition());
    	int fieldDefense = 0;
    	
    	switch (field.getName()) {
    		case "Grass":
    			fieldDefense = 1;
    			break;
    		case "Forest":
    			fieldDefense = 3;
    			break;
    		case "Mountain":
    			fieldDefense = 4;
    			break;
    	}
    	
    	assert fieldDefense != 0;
    	
    	int defense = hp * fieldDefense;
    	
    	int unitDmg = 0;
    	
    	switch (target.getType()) {
    		case "Infantry":
    			unitDmg = 105;
    			break;
    		case "Bazooka":
    			unitDmg = 95;
    			break;
    		case "Jeep":
    			unitDmg = 105;
    			break;
    		case "Light Tank":
    			unitDmg = 85;
    			break;
    		case "Heavy Tank":
    			unitDmg = 55;
    			break;
    		case "Chopper":
    			unitDmg = 75;
    			break;
    	}
    	
    	assert unitDmg != 0;
    	
    	float damage = ((float)(unitDmg - defense)) / 10.0f;
    	
    	hp -= (damage < 1) ? 1 : (int) damage;
    	
    	projectedHP.put(target, hp);
    }

    @SuppressWarnings ("static-access")
    private void tankReposition() {
        for (Entry<String, String> position : tankPositions.entrySet()) {
            
            //Skip repositioning Dead Units
            UnitTile tile = null;
            for (UnitTile tiles : ingameController.unitTiles) {
                if (tiles.getId().equals(position.getKey())) {
                    tile = tiles;
                }
            }
            
            //Skip correctly positioned Units
            if (tile == null || tile.getPosition().equals(position.getValue())) {
                continue;
            }
            
            EnvironmentTile target = ingameController.environmentTileMapById.get(position.getValue());
            Path path = findClosestAccessibleField(tile, target.getX(), target.getY(), true);
            if (path != null) {
            	socket.moveUnit(tile.getId(), path.path);                     
            }
            waitForSocket();
        }
    }

    @Override
    public List<String> requestArmy() {
        String idChopper = "";
        String idHeavyTank = "";
        
        for (Unit unit : availableUnitTypes.values()) {
            if (unit.getType() == "Chopper") {
                idChopper = unit.getId();
            }
            if (unit.getType() == "Heavy Tank") {
                idHeavyTank = unit.getId();
            }
        }
        
        List<String> requestIds = new ArrayList<String>();
        
        for (int i = 0; i < 10; i++) {
            if (i < AMOUNT_HEAVY_TANKS) {
                requestIds.add(idHeavyTank);                
            } else {
                requestIds.add(idChopper);
            }
        }
        
        return requestIds;
    }
    
    class PairComperatorXY implements Comparator<Pair<Integer, Integer>> {

        private int sideLength;
        
        public PairComperatorXY(int length) {
            sideLength = length;
        }
        
        @Override
        public int compare(Pair<Integer, Integer> pairL, Pair<Integer, Integer> pairR) {
            int diff = (pairL.getValue() - pairR.getValue()) * sideLength;
            diff += pairL.getKey() - pairR.getKey();
                
            return diff;
        }
        
    }
}