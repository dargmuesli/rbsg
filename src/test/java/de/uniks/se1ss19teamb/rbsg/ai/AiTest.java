package de.uniks.se1ss19teamb.rbsg.ai;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.modules.ArmyManagerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javafx.util.Pair;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AiTest {
    
    private GameSocket socket;

    private InGameController controller;

    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    /**
     * Mocks socket functions.
     */
    @SuppressWarnings ("static-access")
    @Before
    public void prepareEnvironment() {
        socket = spy(new GameSocket("", "", false));
        doNothing().when(socket).sendToWebsocket(any());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                String targetUnit = invocation.getArgument(0);

                String[] path = invocation.getArgument(1);

                for (UnitTile tile : controller.unitTiles) {
                    if (tile.getId().equals(targetUnit)) {
                        controller.unitTileMapByTileId.remove(tile.getPosition());

                        tile.setPosition(path[path.length - 1]);

                        controller.unitTileMapByTileId.put(path[path.length - 1], tile);
                        break;
                    }
                }

                return null;
            }
        }).when(socket).moveUnit(any(), any());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String targetUnit = invocation.getArgument(1);
                
                for (UnitTile tile : controller.unitTiles) {
                    if (tile.getId().equals(targetUnit)) {
                        controller.unitTiles.remove(tile);
                        break;
                    }
                }
                
                return null;
            }
        }).when(socket).attackUnit(any(), any());
        
        
        
        //Mocking GameSocket to interact with Mock Map
    }

    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    /**
     * Clears the collections that are dependent on a game.
     */
    @SuppressWarnings ("static-access")
    @After
    public void tidyUp() {
        controller.unitTiles.clear();
        controller.unitTileMapByTileId.clear();
        controller.environmentTileMapById.clear();
        controller.environmentTiles.clear();
        controller.inGameObjects.clear();
        ArmyManagerController.availableUnits.clear();
    }
    
    @Test
    public void testInstantiation() {
        AI testInstance = AI.instantiate(-1);
        testInstance.initialize("unittestplayer", socket, controller);
        Assert.assertNotNull(testInstance);
    }
    
    /*
     * Suppress warning, because in near future the relevant fields can't be statically
     * accessed anymore.
     */
    @SuppressWarnings ("static-access")
    @Test
    public void testKaiten() {
        //TODO Replace with proper Mocking / Init once they aren't static anymore
        controller = new InGameController();
        
        //Building Testing Map 4 Wide, 1 Height. Enemy on 0|0, AI on 3|0
        
        EnvironmentTile tile1 = new EnvironmentTile();
        tile1.setId("1");
        tile1.setX(0);
        tile1.setY(0);
        tile1.setPassable(true);
        tile1.setRight("2");
        
        EnvironmentTile tile2 = new EnvironmentTile();
        tile2.setId("2");
        tile2.setX(1);
        tile2.setY(0);
        tile2.setPassable(true);
        tile2.setRight("3");
        tile2.setLeft("1");
        
        EnvironmentTile tile3 = new EnvironmentTile();
        tile3.setId("3");
        tile3.setX(2);
        tile3.setY(0);
        tile3.setPassable(true);
        tile3.setRight("4");
        tile3.setLeft("2");
        
        EnvironmentTile tile4 = new EnvironmentTile();
        tile4.setId("4");
        tile4.setX(3);
        tile4.setY(0);
        tile4.setPassable(true);
        tile4.setLeft("3");
        
        controller.environmentTileMapById.clear();
        controller.environmentTileMapById.put("1", tile1);
        controller.environmentTileMapById.put("2", tile2);
        controller.environmentTileMapById.put("3", tile3);
        controller.environmentTileMapById.put("4", tile4);
        
        Unit unit = new Unit();
        unit.setMp(1);
        unit.setType("unittest");
        ArrayList<String> canAttack = new ArrayList<>();
        canAttack.add("unittest");
        unit.setCanAttack(canAttack);
        
        UnitTile tileEnemy = new UnitTile();
        tileEnemy.setMp(1);
        tileEnemy.setPosition("1");
        tileEnemy.setLeader("enemy");
        tileEnemy.setId("enemy");
        tileEnemy.setType("unittest");
        tileEnemy.setCanAttack(canAttack.toArray(new String[0]));
        
        UnitTile tileAI = new UnitTile();
        tileAI.setMp(1);
        tileAI.setPosition("4");
        tileAI.setLeader("unittestplayer");
        tileAI.setId("AI");
        tileAI.setType("unittest");
        tileAI.setCanAttack(canAttack.toArray(new String[0]));
        
        controller.unitTiles.clear();
        controller.unitTiles.add(tileEnemy);
        controller.unitTiles.add(tileAI);
        
        controller.unitTileMapByTileId.clear();
        controller.unitTileMapByTileId.put("1", tileEnemy);
        controller.unitTileMapByTileId.put("4", tileAI);
        
        ArmyManagerController.availableUnits.clear();
        ArmyManagerController.availableUnits.put("unittest", unit);
        
        InGamePlayer ai = new InGamePlayer();
        ai.setName("unittestplayer");
        
        InGamePlayer enemy = new InGamePlayer();
        enemy.setName("enemy");
        
        controller.inGameObjects.clear();
        controller.inGameObjects.put("unittestplayer", ai);
        controller.inGameObjects.put("enemy", enemy);
        
        AI testInstance = AI.instantiate(-1);
        testInstance.initialize("unittestplayer", socket, controller);
        Assert.assertNotNull(testInstance);
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                Assert.assertEquals("4", tile.getPosition());
            }
        }
        
        testInstance.doTurn();
        testInstance.waitForTurn();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("3", tile.getPosition());
            }
        }
        
        testInstance.doTurn();
        testInstance.waitForTurn();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("2", tile.getPosition());
            }
            
            if (tile.getId().equals("enemy")) {
                //Check if AI attacked
                Assert.fail();
            }
        }
        
    }
    
    @SuppressWarnings ("static-access")
    @Test
    public void testNagato() {
        //TODO Replace with proper Mocking / Init once they aren't static anymore
        controller = new InGameController();
        
        //Building Testing Map 4 Wide, 1 Height. Enemy on 0|0, AI on 3|0
        
        EnvironmentTile tile1 = new EnvironmentTile();
        tile1.setId("Grass@1");
        tile1.setX(0);
        tile1.setY(0);
        tile1.setPassable(true);
        tile1.setRight("Grass@2");
        tile1.setBottom("Grass@3");
        
        EnvironmentTile tile2 = new EnvironmentTile();
        tile2.setId("Grass@2");
        tile2.setX(1);
        tile2.setY(0);
        tile2.setPassable(true);
        tile2.setBottom("Grass@4");
        tile2.setLeft("Grass@1");
        tile2.setRight("Forest@5");
        
        EnvironmentTile tile3 = new EnvironmentTile();
        tile3.setId("Grass@3");
        tile3.setX(0);
        tile3.setY(1);
        tile3.setPassable(true);
        tile3.setRight("Grass@4");
        tile3.setTop("Grass@1");
        
        EnvironmentTile tile4 = new EnvironmentTile();
        tile4.setId("Grass@4");
        tile4.setX(1);
        tile4.setY(1);
        tile4.setPassable(true);
        tile4.setLeft("Grass@3");
        tile4.setTop("Grass@2");
        tile4.setRight("Grass@6");
        
        EnvironmentTile tile5 = new EnvironmentTile();
        tile5.setId("Forest@5");
        tile5.setX(2);
        tile5.setY(0);
        tile5.setPassable(true);
        tile5.setLeft("Grass@2");
        tile5.setBottom("Forest@5");
        
        EnvironmentTile tile6 = new EnvironmentTile();
        tile6.setId("Grass@6");
        tile6.setX(2);
        tile6.setY(1);
        tile6.setPassable(true);
        tile6.setLeft("Grass@4");
        tile6.setTop("Forest@5");
        tile6.setBottom("Grass@9");
        
        EnvironmentTile tile7 = new EnvironmentTile();
        tile7.setId("Grass@7");
        tile7.setX(0);
        tile7.setY(2);
        tile7.setPassable(true);
        tile7.setRight("Grass@8");
        tile7.setTop("Grass@3");
        
        EnvironmentTile tile8 = new EnvironmentTile();
        tile8.setId("Grass@8");
        tile8.setX(1);
        tile8.setY(2);
        tile8.setPassable(true);
        tile8.setLeft("Grass@7");
        tile8.setTop("Grass@4");
        tile8.setRight("Grass@9");
        
        EnvironmentTile tile9 = new EnvironmentTile();
        tile9.setId("Grass@9");
        tile9.setX(2);
        tile9.setY(2);
        tile9.setPassable(true);
        tile9.setLeft("Grass@8");
        tile9.setTop("Grass@6");
        
        controller.environmentTileMapById.clear();
        controller.environmentTileMapById.put("Grass@1", tile1);
        controller.environmentTileMapById.put("Grass@2", tile2);
        controller.environmentTileMapById.put("Grass@3", tile3);
        controller.environmentTileMapById.put("Grass@4", tile4);
        controller.environmentTileMapById.put("Forest@5", tile5);
        controller.environmentTileMapById.put("Grass@6", tile6);
        controller.environmentTileMapById.put("Grass@7", tile7);
        controller.environmentTileMapById.put("Grass@8", tile8);
        controller.environmentTileMapById.put("Grass@9", tile9);
        
        controller.environmentTiles.clear();
        controller.environmentTiles.put(new Pair<>(0,0), tile1);
        controller.environmentTiles.put(new Pair<>(1,0), tile2);
        controller.environmentTiles.put(new Pair<>(0,1), tile3);
        controller.environmentTiles.put(new Pair<>(1,1), tile4);
        controller.environmentTiles.put(new Pair<>(2,0), tile5);
        controller.environmentTiles.put(new Pair<>(2,1), tile6);
        controller.environmentTiles.put(new Pair<>(0,2), tile7);
        controller.environmentTiles.put(new Pair<>(1,2), tile8);
        controller.environmentTiles.put(new Pair<>(2,2), tile9);
        
        Unit unit = new Unit();
        unit.setMp(1);
        unit.setType("Heavy Tank");
        ArrayList<String> canAttack = new ArrayList<>();
        canAttack.add("Heavy Tank");
        canAttack.add("Bazooka Trooper");
        unit.setCanAttack(canAttack);
        unit.setId("tank");

        Unit heli = new Unit();
        heli.setMp(1);
        heli.setType("Chopper");
        heli.setCanAttack(canAttack);
        heli.setId("heli");
        
        UnitTile tileEnemy = new UnitTile();
        tileEnemy.setMp(1);
        tileEnemy.setHp(1);
        tileEnemy.setPosition("Grass@3");
        tileEnemy.setLeader("enemy");
        tileEnemy.setId("enemy");
        tileEnemy.setType("Bazooka Trooper");
        tileEnemy.setCanAttack(canAttack.toArray(new String[0]));
        
        UnitTile tileAI = new UnitTile();
        tileAI.setMp(1);
        tileAI.setPosition("Grass@6");
        tileAI.setLeader("unittestplayer");
        tileAI.setId("AI");
        tileAI.setType("Heavy Tank");
        tileAI.setCanAttack(canAttack.toArray(new String[0]));
        
        controller.unitTiles.clear();
        controller.unitTiles.add(tileAI);
        
        controller.unitTileMapByTileId.clear();
        controller.unitTileMapByTileId.put("Grass@6", tileAI);
        
        ArmyManagerController.availableUnits.clear();
        ArmyManagerController.availableUnits.put("Heavy Tank", unit);
        ArmyManagerController.availableUnits.put("Chopper", heli);
        
        InGamePlayer ai = new InGamePlayer();
        ai.setName("unittestplayer");
        
        InGamePlayer enemy = new InGamePlayer();
        enemy.setName("enemy");
        
        controller.inGameObjects.clear();
        controller.inGameObjects.put("unittestplayer", ai);
        controller.inGameObjects.put("enemy", enemy);
        
        Nagato testInstance = (Nagato)AI.instantiateStrategic(1);
        
        List<String> request = testInstance.requestArmy();
        Assert.assertTrue(request.contains("tank"));
        Assert.assertTrue(request.contains("heli"));
        
        testInstance.AMOUNT_HEAVY_TANKS = 1;
        testInstance.initialize("unittestplayer", socket, controller);
        
        Assert.assertFalse(testInstance.tankPositions.isEmpty());
        Entry<String, String> tankPosition = testInstance.tankPositions.entrySet().iterator().next();
        Assert.assertEquals("AI", tankPosition.getKey());
        Assert.assertEquals("Forest@5", tankPosition.getValue());
        
        socket.phaseString = "Not Movement Phase";
        
        testInstance.doTurnInternal();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("Forest@5", tile.getPosition());
            }
        }
        
        tileAI.setMp(4);
        
        controller.unitTiles.add(tileEnemy);
        controller.unitTileMapByTileId.put("Grass@3", tileEnemy);
        
        
        testInstance.doTurnInternal();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("Forest@5", tile.getPosition());
            }
            
            if (tile.getId().equals("enemy")) {
                //Check if AI attacked
                Assert.fail();
            }
        }
        
        tileAI.setId("NowAChopper!!");
        tileAI.setType("Chopper");
        
        controller.unitTiles.add(tileEnemy);
        controller.unitTileMapByTileId.put("Grass@3", tileEnemy);
        
        testInstance.doTurnInternal();
        
        for (UnitTile tile : controller.unitTiles) {
            
            if (tile.getId().equals("enemy")) {
                //Check if AI attacked
                Assert.fail();
            }
        }
    }
    
    @SuppressWarnings ("static-access")
    @Test
    public void testKongo() {
        //TODO Replace with proper Mocking / Init once they aren't static anymore
        controller = new InGameController();
        
        //Building Testing Map 4 Wide, 1 Height. Enemy on 0|0, AI on 3|0
        
        EnvironmentTile tile1 = new EnvironmentTile();
        tile1.setId("Grass@1");
        tile1.setX(0);
        tile1.setY(0);
        tile1.setPassable(true);
        tile1.setRight("Grass@2");
        tile1.setBottom("Grass@3");
        
        EnvironmentTile tile2 = new EnvironmentTile();
        tile2.setId("Grass@2");
        tile2.setX(1);
        tile2.setY(0);
        tile2.setPassable(true);
        tile2.setBottom("Grass@4");
        tile2.setLeft("Grass@1");
        tile2.setRight("Forest@5");
        
        EnvironmentTile tile3 = new EnvironmentTile();
        tile3.setId("Grass@3");
        tile3.setX(0);
        tile3.setY(1);
        tile3.setPassable(true);
        tile3.setRight("Grass@4");
        tile3.setTop("Grass@1");
        
        EnvironmentTile tile4 = new EnvironmentTile();
        tile4.setId("Grass@4");
        tile4.setX(1);
        tile4.setY(1);
        tile4.setPassable(true);
        tile4.setLeft("Grass@3");
        tile4.setTop("Grass@2");
        tile4.setRight("Grass@6");
        
        EnvironmentTile tile5 = new EnvironmentTile();
        tile5.setId("Forest@5");
        tile5.setX(2);
        tile5.setY(0);
        tile5.setPassable(true);
        tile5.setLeft("Grass@2");
        tile5.setBottom("Forest@5");
        
        EnvironmentTile tile6 = new EnvironmentTile();
        tile6.setId("Grass@6");
        tile6.setX(2);
        tile6.setY(1);
        tile6.setPassable(true);
        tile6.setLeft("Grass@4");
        tile6.setTop("Forest@5");
        tile6.setBottom("Grass@9");
        
        EnvironmentTile tile7 = new EnvironmentTile();
        tile7.setId("Grass@7");
        tile7.setX(0);
        tile7.setY(2);
        tile7.setPassable(true);
        tile7.setRight("Grass@8");
        tile7.setTop("Grass@3");
        
        EnvironmentTile tile8 = new EnvironmentTile();
        tile8.setId("Grass@8");
        tile8.setX(1);
        tile8.setY(2);
        tile8.setPassable(true);
        tile8.setLeft("Grass@7");
        tile8.setTop("Grass@4");
        tile8.setRight("Grass@9");
        
        EnvironmentTile tile9 = new EnvironmentTile();
        tile9.setId("Grass@9");
        tile9.setX(2);
        tile9.setY(2);
        tile9.setPassable(true);
        tile9.setLeft("Grass@8");
        tile9.setTop("Grass@6");
        
        controller.environmentTileMapById.clear();
        controller.environmentTileMapById.put("Grass@1", tile1);
        controller.environmentTileMapById.put("Grass@2", tile2);
        controller.environmentTileMapById.put("Grass@3", tile3);
        controller.environmentTileMapById.put("Grass@4", tile4);
        controller.environmentTileMapById.put("Forest@5", tile5);
        controller.environmentTileMapById.put("Grass@6", tile6);
        controller.environmentTileMapById.put("Grass@7", tile7);
        controller.environmentTileMapById.put("Grass@8", tile8);
        controller.environmentTileMapById.put("Grass@9", tile9);
        
        controller.environmentTiles.clear();
        controller.environmentTiles.put(new Pair<>(0,0), tile1);
        controller.environmentTiles.put(new Pair<>(1,0), tile2);
        controller.environmentTiles.put(new Pair<>(0,1), tile3);
        controller.environmentTiles.put(new Pair<>(1,1), tile4);
        controller.environmentTiles.put(new Pair<>(2,0), tile5);
        controller.environmentTiles.put(new Pair<>(2,1), tile6);
        controller.environmentTiles.put(new Pair<>(0,2), tile7);
        controller.environmentTiles.put(new Pair<>(1,2), tile8);
        controller.environmentTiles.put(new Pair<>(2,2), tile9);
        
        Unit unit = new Unit();
        unit.setMp(1);
        unit.setType("Heavy Tank");
        ArrayList<String> canAttack = new ArrayList<>();
        canAttack.add("Heavy Tank");
        canAttack.add("Bazooka Trooper");
        unit.setCanAttack(canAttack);
        unit.setId("tank");

        Unit heli = new Unit();
        heli.setMp(1);
        heli.setType("Chopper");
        heli.setCanAttack(canAttack);
        heli.setId("heli");
        
        UnitTile tileEnemy = new UnitTile();
        tileEnemy.setMp(1);
        tileEnemy.setHp(1);
        tileEnemy.setPosition("Grass@3");
        tileEnemy.setLeader("enemy");
        tileEnemy.setId("enemy");
        tileEnemy.setType("Bazooka Trooper");
        tileEnemy.setCanAttack(canAttack.toArray(new String[0]));
        
        UnitTile tileAI = new UnitTile();
        tileAI.setMp(1);
        tileAI.setPosition("Grass@6");
        tileAI.setLeader("unittestplayer");
        tileAI.setId("AI");
        tileAI.setType("Heavy Tank");
        tileAI.setCanAttack(canAttack.toArray(new String[0]));
        
        controller.unitTiles.clear();
        controller.unitTiles.add(tileAI);
        
        controller.unitTileMapByTileId.clear();
        controller.unitTileMapByTileId.put("Grass@6", tileAI);
        
        ArmyManagerController.availableUnits.clear();
        ArmyManagerController.availableUnits.put("Heavy Tank", unit);
        ArmyManagerController.availableUnits.put("Chopper", heli);
        
        InGamePlayer ai = new InGamePlayer();
        ai.setName("unittestplayer");
        
        InGamePlayer enemy = new InGamePlayer();
        enemy.setName("enemy");
        
        controller.inGameObjects.clear();
        controller.inGameObjects.put("unittestplayer", ai);
        controller.inGameObjects.put("enemy", enemy);
        
        Kongo testInstance = (Kongo)AI.instantiate(0);
        
        List<String> request = testInstance.requestArmy();
        Assert.assertNull(request);
        
        testInstance.initialize("unittestplayer", socket, controller);
        
        socket.phaseString = "Not Movement Phase";
        
        tileAI.setMp(4);
        
        controller.unitTiles.add(tileEnemy);
        controller.unitTileMapByTileId.put("Grass@3", tileEnemy);
        
        
        testInstance.doTurnInternal();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("Grass@7", tile.getPosition());
            }
            
            if (tile.getId().equals("enemy")) {
                //Check if AI attacked
                Assert.fail();
            }
        }
    }
}
