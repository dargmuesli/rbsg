package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.testmodel.Game; //TODO needs to be changed when we implement the game

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fulib.yaml.YamlIdMap;


public class PersistenceUtil {

    public static final String SAVEGAME_YAML = "./savegame.yaml";
    private YamlIdMap yamlIdMap = new YamlIdMap(Game.class.getPackage().getName());
    private ErrorHandler errorHandler = new ErrorHandler();

    private static final Logger logger = LogManager.getLogger(PersistenceUtil.class);

    public void save(Game game) {

        String yaml = yamlIdMap.encode(game);

        try {

            File file = new File(SAVEGAME_YAML);

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(file.toURI()), yaml.getBytes("UTF-8"));

        } catch (IOException e) {
            errorHandler.sendError("Fehler konnte nicht in Datei schreiben!");
            logger.error(e);
        }
    }

    private Game load() {
        File file = new File(SAVEGAME_YAML);

        if (!file.exists()) {
            return null;
        }

        Game game = new Game();

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
            String yaml = new String(bytes, "UTF-8");
            yamlIdMap.decode(yaml, game);
            return game;

        } catch (IOException e) {
            errorHandler.sendError("Fehler konnte die Bytes nicht lesen!");
            logger.error(e);
            return null;
        }
    }
    
    public Game loader() {
        PersistenceUtil p = new PersistenceUtil();
        return p.load();
    }
}
