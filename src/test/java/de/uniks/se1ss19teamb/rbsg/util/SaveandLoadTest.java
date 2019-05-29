package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.testmodel.Game;
import de.uniks.se1ss19teamb.rbsg.testmodel.Platform;
import de.uniks.se1ss19teamb.rbsg.testmodel.Player;
import de.uniks.se1ss19teamb.rbsg.testmodel.Unit;
import de.uniks.se1ss19teamb.rbsg.util.PersistenceUtil;

import org.junit.Test;


import java.io.File;

import static org.junit.Assert.assertEquals;


public class SaveandLoadTest {

    @Test
    public void saveandloadTest() {
        
        Game game = new Game();
        Player alice = new Player().setName("Alice");
        Platform p1 = new Platform();
        Platform p2 = new Platform();
        game.withPlayers(alice).withPlatforms(p1, p2);
        p1.setCapacity(5);
        p2.setCapacity(3);
        alice.withPlatforms(p1, p2);
        for(int i = 0; i < 5; i++) {
            alice.withUnits(new Unit());
            alice.getUnits().get(i).setPlatform(p1);
        }
        p1.withNeighbors(p2);

        PersistenceUtil p = new PersistenceUtil();
        p.save(game);

        Game loadedGame = p.loader();

        assertEquals(loadedGame.getPlayers().get(0).getName(), game.getPlayers().get(0).getName());
        assertEquals(loadedGame.getPlatforms().get(0).getCapacity(), game.getPlatforms().get(0).getCapacity());
        assertEquals(loadedGame.getPlatforms().get(1).getCapacity(), game.getPlatforms().get(1).getCapacity());

        File file = new File("./savegame.yaml");
        file.delete();
    }
}
