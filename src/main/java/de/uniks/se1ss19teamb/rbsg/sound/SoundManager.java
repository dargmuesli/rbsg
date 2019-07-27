package de.uniks.se1ss19teamb.rbsg.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance = null;
    private Map<String, Sound> sounds = new HashMap<>();

    private SoundManager() {
    }

    public static void init() {
        instance = new SoundManager();

        instance.sounds.put("meme_bad_bitch", new Sound("bad_bitch.wav", true));
        instance.sounds.put("meme_beer", new Sound("beer.wav", true));

        instance.sounds.put("BazookaTrooper", new Sound("bazooka1.wav",true));
        instance.sounds.put("BazookaTrooper2", new Sound("bazooka2.wav",true));
        instance.sounds.put("BazookaTrooper_Move", new Sound("beer.wav",true));

        instance.sounds.put("Chopper", new Sound("chooper.wav",true));
        instance.sounds.put("Chopper_Move", new Sound("beer.wav",true));


        instance.sounds.put("Jeep", new Sound("jeep.wav",true));
        instance.sounds.put("Jeep_Move", new Sound("beer.wav",true));

        instance.sounds.put("LightTank", new Sound("lightTank.wav",true));
        instance.sounds.put("LightTank_Move", new Sound("beer.wav",true));

        instance.sounds.put("Infantry", new Sound("infantry_fire.wav",true));
        instance.sounds.put("Infantry_Move", new Sound("infantry_move.wav",true));

        instance.sounds.put("HeavyTank",new Sound("panzer.wav", true));
        instance.sounds.put("HeavyTank_Move",new Sound("beer.wav", true));

        instance.sounds.put("nani", new Sound("nani.wav", true));
        instance.sounds.put("Omae", new Sound("Omae_wa_mou_shindeiru.wav", true));
        instance.sounds.put("Omae_nani", new Sound("Omae_wa_mou_shindeiru_nani.wav", true));

        //https://www.youtube.com/watch?v=VZec_PqXXzo
        //If anyone feels offended: Fight me
        Sound bgm = new Sound("katyusha.mp3", false);
        instance.sounds.put("bgm", bgm);

        instance.sounds.put("missing", new Sound("error.wav", true));
        instance.sounds.put("missing2", new Sound("fail-trombone-01.wav", true));
    }

    public static void playSound(String toFetch, float balance) {
        instance.fetchSound(toFetch).play(balance);
    }

    public static boolean getLoaded(String toFetch) {
        Sound sound = instance.fetchSound(toFetch);
        return sound.keepInRam;
    }

    private Sound fetchSound(String toFetch) {
        Sound sound = sounds.get(toFetch);

        if (sound == null) {
            sound = sounds.get("missing");
        }

        return sound;
    }
}
