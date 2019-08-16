package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AI {
    
    //AI Attributes
    
    protected static final Logger logger = LogManager.getLogger();
    
    protected String playerID;
    protected GameSocket socket;
    protected InGameController ingameController;
    protected Map<String, Unit> availableUnitTypes;
    private Thread aiObsThread;
    
    public AI() {
        this.availableUnitTypes = ArmyManagerController.availableUnits;
    }
    
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
        this.playerID = playerID;
        this.socket = socket;
        this.ingameController = controller;
    }
    
    protected abstract void doTurnInternal();
    
    public void doTurn() {
        aiObsThread = new Thread(() -> {
            Thread aiThread = new Thread(() -> {
                
                doTurnInternal();
                
                aiObsThread.interrupt();
            }, "AI-Thread");
            aiThread.start();
            
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                return;
            }
            
            logger.warn("AI Timeout!");
            aiThread.stop();
        }, "AI-Observer-Thread");
        aiObsThread.start();
    }
    
    public void waitForTurn() {
        try {
            aiObsThread.join();
        } catch (InterruptedException e) {
            logger.warn("Waiting for AI-Observer-Thread was interrupted");
        }
    }
    
    public abstract List<String> requestArmy();
    
    //Helper Functions
    
    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    @SuppressWarnings ("static-access")
    protected Path findClosestAccessibleField(UnitTile unit, int x, int y, boolean onTop) {
        ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                unit.getPosition()), unit.getMp(), false);
        
        if (ingameController.previousTileMapById.isEmpty()) {
            return null;
        }
        
        String closest = null;
        int closestDistance = Integer.MAX_VALUE;
        
        for (String targetTile : ingameController.previousTileMapById.keySet()) {
            EnvironmentTile current = ingameController.environmentTileMapById.get(targetTile);
            
            int currentDistance = Math.abs(x - current.getX()) + Math.abs(y - current.getY());
            
            //Forbid walking onto the target
            if (currentDistance < closestDistance && (onTop || currentDistance > 0)) {
                closestDistance = currentDistance;
                closest = current.getId();
            }
        }
        
        Path path = new Path();
        path.end = ingameController.environmentTileMapById.get(closest);
        path.start = ingameController.environmentTileMapById.get(unit.getPosition());        
        path.distance = closestDistance;
        
        LinkedList<String> pathList = new LinkedList<>();

        do {
            pathList.addFirst(closest);
            closest = ingameController.previousTileMapById.get(closest);
        } while (!closest.equals(unit.getPosition()));

        path.path = pathList.toArray(new String[0]);
        
        return path;
    }
    
    @SuppressWarnings ("static-access")
    protected TreeMap<Path, UnitTile> findAllAttackableEnemies(UnitTile unit) {
    	TreeMap<Path, UnitTile> attackable = new TreeMap<>((pathL, pathR) -> (pathL.distance - pathR.distance));
    	
        ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                unit.getPosition()), unit.getMp(), false);
        
        if (ingameController.previousTileAttackMapById.isEmpty()) {
            return attackable;
        }
         
        for (String attackableTile : ingameController.previousTileAttackMapById.keySet()) {
        	UnitTile toAttack = ingameController.unitTileMapByTileId.get(attackableTile);
        	EnvironmentTile toAttackFrom = ingameController.environmentTileMapById.get(
        			ingameController.previousTileAttackMapById.get(attackableTile));
        	
        	Path path = new Path();
            path.end = toAttackFrom;
            path.start = ingameController.environmentTileMapById.get(unit.getPosition());        
            path.distance = 0;
            
            LinkedList<String> pathList = new LinkedList<>();

            String closest = toAttackFrom.getId();
            
            do {
                pathList.addFirst(closest);
                path.distance++;
                closest = ingameController.previousTileMapById.get(closest);
            } while (!closest.equals(unit.getPosition()));

            path.path = pathList.toArray(new String[0]);
            
            attackable.put(path, toAttack);
        }
        
        return attackable;
    }
    
    protected void waitForSocket() {
        //TODO Proper "Wait-For-Socket"
        try {
            Thread.yield();
            Thread.sleep(500);
            System.out.println("Waiting for Websocket");
        } catch (InterruptedException e) {
            logger.warn("Waiting for Socket failed");
        }
    }
    
    //Global AI access Management
    
    private static SortedMap<Integer, Class<? extends AI>> aiModels = new TreeMap<>();
    
    static {
        aiModels.put(-1, Kaiten.class);
        aiModels.put(0, Nagato.class);
    }
    
    public static AI instantiate(int difficulty) {
        difficulty = (difficulty == Integer.MAX_VALUE) ? aiModels.lastKey() : difficulty;
        Class<? extends AI> targetAI = aiModels.get(difficulty);
        targetAI = (targetAI == null) ? aiModels.get(-1) : targetAI;
        try {
            return targetAI.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static int getHighestDifficulty() {
        return aiModels.lastKey();
    }
    
}
