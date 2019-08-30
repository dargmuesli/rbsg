package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.ThreadLocks;

import java.util.*;

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
                Thread.sleep(200000);
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
    @SuppressWarnings("static-access")
    protected Pair<Path, Integer> findClosestAccessibleField(UnitTile unit, int x, int y, boolean onTop) {

        ingameController.drawOverlay(ingameController.environmentTileMapById.get(
            unit.getPosition()), unit.getMpLeft(), false,
            ((InGamePlayer) ingameController.inGameObjects
                .get(unit.getLeader())).getName());

        if (ingameController.previousTileMapById.isEmpty()) {
            return null;
        }

        String closest = null;
        int closestDistance = Integer.MAX_VALUE;

        ThreadLocks.getReadLockPreviousTileMapById().lock();
        Map<String, String> clonedPreviousTileMap = new HashMap<>(ingameController.previousTileMapById);

        ThreadLocks.getReadEnvironmentTileMapById().lock();
        Map<String, EnvironmentTile> clonedEnvironmentTileMap = new HashMap<>(ingameController.environmentTileMapById);

        for (String targetTile : clonedPreviousTileMap.keySet()) {
            EnvironmentTile current = clonedEnvironmentTileMap.get(targetTile);

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
        path.distance = 0;

        LinkedList<String> pathList = new LinkedList<>();

        do {
            path.distance++;
            pathList.addFirst(closest);
            closest = ingameController.previousTileMapById.get(closest);
            // NullPointerException is thrown here.
        } while (!closest.equals(unit.getPosition()));

        path.path = pathList.toArray(new String[0]);

        ThreadLocks.getReadLockPreviousTileMapById().unlock();
        ThreadLocks.getReadEnvironmentTileMapById().unlock();

        return new Pair<>(path, closestDistance);
    }

    @SuppressWarnings("static-access")
    protected TreeMap<Path, UnitTile> findAllAttackableEnemies(UnitTile unit) {
        TreeMap<Path, UnitTile> attackable = new TreeMap<>((pathL, pathR) -> (pathL.distance - pathR.distance));

        ingameController.drawOverlay(ingameController.environmentTileMapById.get(
            unit.getPosition()), unit.getMpLeft(), false,
            ((InGamePlayer) ingameController.inGameObjects
                .get(unit.getLeader())).getName());

        if (ingameController.previousTileAttackMapById.isEmpty()) {
            return attackable;
        }

        for (String attackableTile : ingameController.previousTileAttackMapById.keySet()) {
            EnvironmentTile toAttackFrom = ingameController.environmentTileMapById.get(
                ingameController.previousTileAttackMapById.get(attackableTile));

            Path path = new Path();
            path.end = toAttackFrom;
            path.start = ingameController.environmentTileMapById.get(unit.getPosition());
            path.distance = 0;

            LinkedList<String> pathList = new LinkedList<>();

            String closest = toAttackFrom.getId();

            if (ingameController.previousTileMapById.isEmpty()) {
                attackable.put(path, ingameController.unitTileMapByTileId.get(attackableTile));
                return attackable;
            }

            do {
                pathList.addFirst(closest);
                path.distance++;
                closest = ingameController.previousTileMapById.get(closest);
            } while (!closest.equals(unit.getPosition()));

            path.path = pathList.toArray(new String[0]);

            attackable.put(path, ingameController.unitTileMapByTileId.get(attackableTile));
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
    private static SortedMap<Integer, Class<? extends AI>> aiModelsStrategic = new TreeMap<>();


    static {
        aiModels.put(-1, Kaiten.class);

        aiModelsStrategic.put(-1, Kaiten.class);
        aiModelsStrategic.put(0, Nagato.class);
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

    public static AI instantiateStrategic(int difficulty) {
        difficulty = (difficulty == Integer.MAX_VALUE) ? aiModelsStrategic.lastKey() : difficulty;
        Class<? extends AI> targetAI = aiModelsStrategic.get(difficulty);
        targetAI = (targetAI == null) ? aiModelsStrategic.get(-1) : targetAI;
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
