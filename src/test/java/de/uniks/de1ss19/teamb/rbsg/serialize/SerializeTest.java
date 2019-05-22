package de.uniks.de1ss19.teamb.rbsg.serialize;

import com.google.gson.Gson;
import de.uniks.se1ss19teamb.rbsg.serialize.SerializeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SerializeTest {
    /*
    private class Game {

        //Fields defined by Server
        private long joinedPlayers;
        private String name;
        private String id;
        private long neededPlayers;

        public long getJoinedPlayers() {
            return joinedPlayers;
        }
        public void setJoinedPlayers(Long joinedPlayers) {
            this.joinedPlayers = joinedPlayers;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public long getNeededPlayers() {
            return neededPlayers;
        }
        public void setNeededPlayers(long neededPlayers) {
            this.neededPlayers = neededPlayers;
        }
    }


     */
    @Test
    public void serializeTest() throws FileNotFoundException {
        SerializeUtils.Game game1 = new SerializeUtils.Game();
        game1.setJoinedPlayers(2L);
        game1.setName("Blabla");
        game1.setId("12345MYID");
        game1.setNeededPlayers(4);

        SerializeUtils.serialize("file.json", game1);
        SerializeUtils.Game game2 = new SerializeUtils.Game();
        game2 = SerializeUtils.deserialize("file.json", new SerializeUtils.Game());

        Assert.assertEquals(game1.getName(), game2.getName());
        Assert.assertEquals(game1.getId(), game2.getId());
        Assert.assertEquals(game1.getJoinedPlayers(), game2.getJoinedPlayers());
        Assert.assertEquals(game1.getNeededPlayers(), game2.getNeededPlayers());
    }
}
