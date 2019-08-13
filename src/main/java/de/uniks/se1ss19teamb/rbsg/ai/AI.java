package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.util.Pair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AI {
    
    //AI Attributes
    
    protected static final Logger logger = LogManager.getLogger();
    
    protected String playerID;
    protected GameSocket socket;
    protected InGameController ingameController;
    protected Map<String, Unit> availableUnitTypes;
    
    public AI() {
    }
    
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
        this.playerID = playerID;
        this.socket = socket;
        this.ingameController = controller;
        this.availableUnitTypes = ArmyManagerController.availableUnits;
    }
    
    public abstract void doTurn();
    
    public abstract Army requestArmy();
    
    //Helper Functions
    
    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    @SuppressWarnings ("static-access")
    protected Pair<Path, Integer> findClosestAccessibleField(UnitTile unit, int x, int y) {
        ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                unit.getPosition()), unit.getMp(), false);
        
        String closest = null;
        int closestDistance = Integer.MAX_VALUE;
        
        for (String targetTile : ingameController.previousTileMapById.keySet()) {
            EnvironmentTile current = ingameController.environmentTileMapById.get(targetTile);
            
            int currentDistance = Math.abs(x - current.getX()) + Math.abs(y - current.getY());
            
            //Forbid walking onto the target
            if (currentDistance < closestDistance && currentDistance > 0) {
                closestDistance = currentDistance;
                closest = current.getId();
            }
        }
        
        Path path = new Path();
        path.end = ingameController.environmentTileMapById.get(closest);
        path.start = ingameController.environmentTileMapById.get(unit.getPosition());        
        
        LinkedList<String> pathList = new LinkedList<>();

        do {
            pathList.addFirst(closest);
            closest = ingameController.previousTileMapById.get(closest);
        } while (!closest.equals(unit.getPosition()));

        path.path = pathList.toArray(new String[0]);
        
        return new Pair<Path, Integer>(path, closestDistance);
    }
    
    protected void waitForSocket() {
        //TODO Proper "Wait-For-Socket"
        try {
            Thread.sleep(100);
            System.out.println("Waiting for Websocket");
        } catch (InterruptedException e) {
            logger.warn("Waiting for Socket failed");
        }
    }
    
    //Global AI access Management
    
    private static SortedMap<Integer, Class<? extends AI>> aiModels = new TreeMap<>();
    
    static {
        aiModels.put(-1, Kaiten.class);
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
    
}
