package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.test.saveTest.testmodel.Game; //needs to be changed when we implement the game
import org.fulib.yaml.YamlIdMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PersistenceUtil {

	public static void main(String[] args) {
		PersistenceUtil p = new PersistenceUtil();
		Game game = p.load();

		System.out.print(game.getPlayers());
	}

	public static final String SAVEGAME_YAML = "./savegame.yaml";
	private YamlIdMap yamlIdMap = new YamlIdMap(Game.class.getPackage().getName());

	public void save(Game game) {

		String yaml = yamlIdMap.encode(game);

		try {

			File file = new File(SAVEGAME_YAML);

			if(!file.exists()) {
					file.createNewFile();
			}

			Files.write(Paths.get(file.toURI()), yaml.getBytes("UTF-16"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Game load() {
		File file = new File(SAVEGAME_YAML);

		if(!file.exists()) {
			return null;
		}

		Game game = new Game();

		try {
			byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
			String yaml = new String(bytes, "UTF-16");
			yamlIdMap.decode(yaml, game);
			return game;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Game loader() {
		PersistenceUtil p = new PersistenceUtil();
		return p.load();
	}
}
