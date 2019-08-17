package de.uniks.se1ss19teamb.rbsg.sockets;

import java.util.ArrayList;

// Distributes the GameSocket instances so any class can take the GameSocket that it needs. Whether it's the
// GameSocket for the main player or for one of the up to 3 extra players that are bots.
// The main GameSocket should be on place 0.
public class GameSocketDistributor {

    private static ArrayList<GameSocket> gameSockets = new ArrayList<>();

    public static void setGameSocket(int number, String gameId, String armyId, boolean spectator) {
        gameSockets.add(number, new GameSocket(gameId, armyId, spectator));
    }

    public static void setGameSocket(int number, String gameId) {
        setGameSocket(number, gameId, null, false);
    }

    public static GameSocket getGameSocket(int number, String gameId, String armyId, boolean spectator) {
        return gameSockets.get(number);
    }

    public static GameSocket getGameSocket(int number, String gameId) {
        return getGameSocket(number, gameId, null, false);
    }

    public static GameSocket getGameSocket(int number) {
        if (gameSockets.size() <= number) {
            //new Exception("Wrong gameSockets ArrayList size!").printStackTrace();
            return null;
        }
        return gameSockets.get(number);
    }
}
