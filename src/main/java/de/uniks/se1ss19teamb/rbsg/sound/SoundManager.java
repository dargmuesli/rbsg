package de.uniks.se1ss19teamb.rbsg.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance = new SoundManager();
    private Map<String, Sound> sounds = new HashMap<>();

    private SoundManager() {
        this.sounds.put("meme_bad_bitch", new Sound("bad_bitch.wav", true));
        this.sounds.put("meme_beer", new Sound("beer.wav", true));

        this.sounds.put("BazookaTrooper", new Sound("bazooka1.wav",true));
        this.sounds.put("BazookaTrooper2", new Sound("bazooka2.wav",true));
        //instance.sounds.put("BazookaTrooper_Move", new Sound(".wav",true));

        this.sounds.put("Chopper", new Sound("chooper.wav",true));
        //instance.sounds.put("Chopper_Move", new Sound(.wav",true));


        this.sounds.put("Jeep", new Sound("jeep.wav",true));
        //instance.sounds.put("Jeep_Move", new Sound(".wav",true));

        this.sounds.put("LightTank", new Sound("lightTank.wav",true));
        //instance.sounds.put("LightTank_Move", new Sound(".wav",true));

        this.sounds.put("Infantry", new Sound("infantry_fire.wav",true));
        this.sounds.put("Infantry_Move", new Sound("infantry_move.wav",true));

        this.sounds.put("HeavyTank",new Sound("panzer.wav", true));
        //instance.sounds.put("HeavyTank_Move",new Sound(".wav", true));

        this.sounds.put("nani", new Sound("nani.wav", true));
        this.sounds.put("Omae", new Sound("Omae_wa_mou_shindeiru.wav", true));
        this.sounds.put("Omae_nani", new Sound("Omae_wa_mou_shindeiru_nani.wav", true));

        //https://www.youtube.com/watch?v=VZec_PqXXzo
        //If anyone feels offended: Fight me
        this.sounds.put("bgm", new Sound("katyusha.wav", false));

        this.sounds.put("missing", new Sound("silence.wav", true));
        this.sounds.put("missing2", new Sound("fail-trombone-01.wav", true));
    }

    public static void playSound(String toFetch, float balance) {
        playSound(toFetch, 1, balance);
    }

    public static void playSound(String toFetch, float volume, float balance) {
        instance.fetchSound(toFetch).play(volume, balance);
    }

    public static boolean getLoaded(String toFetch) {
        return instance.fetchSound(toFetch).keepInRam;
    }

    private Sound fetchSound(String toFetch) {
        return sounds.getOrDefault(toFetch, sounds.get("missing"));
    }
}
