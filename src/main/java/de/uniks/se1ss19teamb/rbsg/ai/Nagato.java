package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

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
     * If Enemy in Attack Range/2 of HT -> Attack and retreat
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
        
        int sideDir = -1;
        boolean sideXY = false;
        
        //Figure out which side of the Board we are on
        {
            EnvironmentTile randomFriendly = null;
        
            for (UnitTile unit : controller.unitTiles) {
                if (unit.getLeader().equals(playerID)) {
                    randomFriendly = controller.environmentTileMapById.get(unit.getPosition());
                    break;
                }
            }
            
            if (randomFriendly.getX() == 0 || randomFriendly.getY() == 0) {
                sideDir = 0;
                sideXY = randomFriendly.getX() == 0;
                
            } else if (randomFriendly.getX() == sideLength || randomFriendly.getY() == sideLength) {
                sideDir = 1;
                sideXY = randomFriendly.getX() == sideLength;
            }
            
            assert sideDir != -1;
        }
        
        //Iterate over our Quarter of the Field, and "normalize" it to our direction
        int startShort = sideDir == 1 ? sideLength - 1 : 0;
        int stopShort = sideLength / 2 - sideDir;
        int lineCnt = 0;
        
        SortedMap<Pair<Integer, Integer>, EnvironmentTile> mapQuarter = new TreeMap<>(new PairComperatorXY(sideLength));
        
        for (int shortSide = startShort; shortSide != stopShort; shortSide -= sideDir * 2 - 1) {
            for (int longSide = lineCnt; longSide < sideLength - lineCnt; longSide++) {
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
    public void doTurn() {
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
        
        //TODO Check Phase Advancement - Tank Movement Needed? Concede?
        
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

    private void tankMovementAttackSafe() {
        // TODO Auto-generated method stub
        
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
            Pair<Path, Integer> path = findClosestAccessibleField(tile, target.getX(), target.getY(), true);
            socket.moveUnit(tile.getId(), path.getKey().path);     
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
            int diff = (pairR.getValue() - pairL.getValue()) * sideLength;
            diff += pairL.getKey() - pairR.getKey();
                
            return diff;
        }
        
    }
}