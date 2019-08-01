package de.uniks.se1ss19teamb.rbsg.features;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class ChuckNorrisJokeTicker {

    private static String[] jokes = {
        "Chuck Norris hat bis zur Unendlichkeit gez\u00E4hlt ... 2-mal.", "Chuck Norris kann schwarze "
            + "Filzstifte nach Farbe sortieren.", "Chuck Norris hat alle Farben erfunden. Außer Rosa! "
            + "Tom Cruise hat Rosa erfunden.",
        "Einige Leute tragen Superman Schlafanz\u00FCge. Superman tr\u00E4gt Chuck Norris Schlafanz\u00FCge.",
        "Chuck Norris kann ein Feuer entfachen, indem er zwei Eisw\u00FCrfel "
            + "aneinander reibt.", "Chuck Norris kann Dreht\u00FCren "
            + "zuschlagen!", "Manche Menschen k\u00F6nnen viele Liegest\u00FCtzen — Chuck Norris kann alle.",
        "Chuck Norris wurde gestern geblitzt — beim Einparken",
        "Chuck Norris verzichtet auf seine Rechte — seine Linke ist sowieso "
            + "schneller ...", "Chuck Norris kennt die letzte Ziffer von Pi.", "Chuck Norris trinkt seinen Kaffee am "
            + "liebsten schwarz. Ohne Wasser.", "Chuck Norris wurde letztens von der Polizei angehalten ... — Die "
            + "Polizisten sind mit einer Verwarnung davon gekommen.",
        "Chuck Norris ist Fallschirmspringen gegangen. Sein"
            + "Fallschirm hat sich nicht ge\u00F6ffnet. Er ist den Fallschirm danach umtauschen gegangen.",
        "Arnold Schwarzenegger musste wegen schweren Verletzungen ins Krankenhaus eingeliefert werden. "
            + "Chuck Norris hatte "
            + "ihn auf Facebook angestupst."};
    
    private static int random;
    private static AnimationTimer animationTimer;

    public static void moveLabel(Label label) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (label.getLayoutX() != -1000) {
                    label.setLayoutX(label.getLayoutX() - 2);
                } else if (label.getLayoutX() == -1000) {
                    random = (int) (Math.random() * jokes.length);
                    label.setText(jokes[random]);
                    label.setLayoutX(2200);
                }
            }
        };
        animationTimer.start();
    }

    public static void setLabelPosition(Label label) {
        random = (int) (Math.random() * jokes.length);
        label.setText(jokes[random]);
        label.setLayoutX(2200);
        label.setTranslateY(label.getLayoutY() + 75);
    }

    public static void stopAnimation() {
        animationTimer.stop();
    }
}
