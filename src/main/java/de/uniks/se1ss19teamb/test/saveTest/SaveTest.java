package de.uniks.se1ss19teamb.test.saveTest;


import de.uniks.se1ss19teamb.rbsg.PersistenceUtil;
import de.uniks.se1ss19teamb.test.saveTest.testmodel.Game;
import de.uniks.se1ss19teamb.test.saveTest.testmodel.Platform;
import de.uniks.se1ss19teamb.test.saveTest.testmodel.Player;
import de.uniks.se1ss19teamb.test.saveTest.testmodel.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveTest {

	@Test
	public void saveTest() {
		Game game = new Game();
		Player alice = new Player();
		Platform p1 = new Platform();
		Platform p2 = new Platform();
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

		Game loadedGame = p.load();

		assertEquals(loadedGame.getPlayers(), game.getPlayers());

	}
}
