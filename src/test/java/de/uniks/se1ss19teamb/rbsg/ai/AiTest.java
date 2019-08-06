package de.uniks.se1ss19teamb.rbsg.ai;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AiTest {
    
    private GameSocket socket;

    private InGameController controller;
    
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
        
        UnitTile tileAI = new UnitTile();
        tileAI.setMp(1);
        tileAI.setPosition("4");
        tileAI.setLeader("unittestplayer");
        tileAI.setId("AI");
        tileAI.setType("unittest");
        
        controller.unitTiles.clear();
        controller.unitTiles.add(tileEnemy);
        controller.unitTiles.add(tileAI);
        
        controller.unitTileMapByTileId.clear();
        controller.unitTileMapByTileId.put("1", tileEnemy);
        controller.unitTileMapByTileId.put("4", tileAI);
        
        ArmyManagerController.availableUnits.clear();
        ArmyManagerController.availableUnits.put("unittest", unit);
        
        //Mocking GameSocket to interact with Mock Map
    }
    
    @SuppressWarnings ("static-access")
    @After
    public void tidyUp() {
        controller.unitTiles.clear();
        controller.unitTileMapByTileId.clear();
        controller.environmentTileMapById.clear();
        ArmyManagerController.availableUnits.clear();
    }
    
    @Test
    public void testInstantiation() {
        AI testInstance = AI.instantiate("unittestplayer", socket, controller, -1);
        Assert.assertNotNull(testInstance);
    }
    
    @SuppressWarnings ("static-access")
    @Test
    public void testKaiten() {
        AI testInstance = AI.instantiate("unittestplayer", socket, controller, -1);
        Assert.assertNotNull(testInstance);
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                Assert.assertEquals("4", tile.getPosition());
            }
        }
        
        testInstance.doTurn();
        
        for (UnitTile tile : controller.unitTiles) {
            if (tile.getId().equals("AI")) {
                //Check if AI moved
                Assert.assertEquals("3", tile.getPosition());
            }
        }
        
        testInstance.doTurn();
        
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
}
