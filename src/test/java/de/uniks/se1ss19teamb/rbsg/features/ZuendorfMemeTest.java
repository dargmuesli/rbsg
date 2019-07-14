package de.uniks.se1ss19teamb.rbsg.features;

import de.uniks.se1ss19teamb.rbsg.TestUtil;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZuendorfMemeTest {

    @Before
    public void prepareTest() throws ExceptionInInitializerError, InterruptedException {
        TestUtil.initJfx();
        TestUtil.initRandom();

        ZuendorfMeme.QUIET = true;
    }

    @Test
    public void setupTest() {
        Pane root = new Pane();

        Assert.assertEquals(0, root.getChildren().size());

        ZuendorfMeme.setup(root);

        hoverImageView(10);

        Assert.assertEquals(1, root.getChildren().size());

        hoverImageView(15);

        Assert.assertEquals(0, root.getChildren().size());
    }

    private void hoverImageView(int iterations) {
        for (int i = 0; i < iterations; i++) {
            ZuendorfMeme.IMAGE_VIEW.fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED,
                0, 0, 0, 0, MouseButton.NONE, 0,
                false, false, false, false, false, false, false, false, false, false, null));
        }
    }
}
