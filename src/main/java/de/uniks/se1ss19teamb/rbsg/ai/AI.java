package de.uniks.se1ss19teamb.rbsg.ai;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import javafx.util.Pair;

public abstract class AI {
	
	//AI Attributes
	
	protected String playerID;
	protected GameSocket socket;
	protected InGameController ingameController;
	protected Map<String, Unit> availableUnitTypes;
	
	public AI(String playerID) {
		this.playerID = playerID;
		this.socket = GameSocket.instance;
		this.ingameController = InGameController.instance;
		this.availableUnitTypes = ArmyManagerController.availableUnits;
	}
	
	public abstract void doTurn();
	
	//Helper Functions
	
	@SuppressWarnings ("static-access")
	protected Pair<Path, Integer> findClosestAccessibleField(UnitTile unit, int x, int y) {
		ingameController.drawOverlay(ingameController.environmentTileMapById.get(unit.getPosition()), unit.getMp(), false);
		
		String closest = null;
		int closestDistance = Integer.MAX_VALUE;
		
		for(String targetTile : ingameController.previousTileMapById.keySet()) {
			EnvironmentTile current = ingameController.environmentTileMapById.get(targetTile);
			
			int currentDistance = Math.abs(x - current.getX()) + Math.abs(y - current.getY());
			
			//Forbid walking onto the target
			if(currentDistance < closestDistance && currentDistance > 0) {
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
            closest = ingameController.environmentTileMapById.get(ingameController.previousTileMapById.get(closest)).getId();
        } while (!closest.equals(unit.getPosition()));

        path.path = pathList.toArray(new String[0]);
		
		return new Pair<Path, Integer>(path, closestDistance);
	}
	
	protected void waitForSocket() {
		//TODO Proper "Wait-For-Socket"
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { }
	}
	
	//Global AI access Management
	
	private static Map<Integer, Class<? extends AI>> aiModels = new HashMap<>();
	
	static {
		aiModels.put(-1, Kaiten.class);
	}
	
	public static AI instantiate(String playerID, int difficulty) {
		Class<? extends AI> targetAI = aiModels.get(difficulty);
		targetAI = (targetAI == null) ? aiModels.get(-1) : targetAI;
		try {
			Constructor<? extends AI> constructor = targetAI.getConstructor(new Class[]{String.class});
			return constructor.newInstance(new Object[]{playerID}) ; 
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
}
