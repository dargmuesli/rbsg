package de.uniks.se1ss19teamb.rbsg.util;

import fulib.generated.testmodel.Game; //TODO needs to be changed when we implement the game

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fulib.yaml.YamlIdMap;


class PersistenceUtil {

    private static final String SAVEGAME_YAML = "./savegame.yaml";
    private static final Logger logger = LogManager.getLogger();
    private YamlIdMap yamlIdMap = new YamlIdMap(Game.class.getPackage().getName());
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    public void save(Game game) {

        String yaml = yamlIdMap.encode(game);

        try {

            File file = new File(SAVEGAME_YAML);

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(file.toURI()), yaml.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            notificationHandler.sendError("Spielstand konnte nicht in eine Datei geschrieben werden!", logger, e);
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
            String yaml = new String(bytes, StandardCharsets.UTF_8);
            yamlIdMap.decode(yaml, game);
            return game;

        } catch (IOException e) {
            notificationHandler.sendError("Spielstand konnte nicht geladen werden!", logger, e);
            return null;
        }
    }

    public Game loader() {
        PersistenceUtil p = new PersistenceUtil();
        return p.load();
    }
}
