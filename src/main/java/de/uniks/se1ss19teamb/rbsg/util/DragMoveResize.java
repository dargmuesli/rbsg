package de.uniks.se1ss19teamb.rbsg.util;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

class DragMoveResize {

    private static final int RESIZE_MARGIN = 5;

    private final Region region;

    private boolean moving;
    private boolean dragging;

    private Cursor usedCursor;

    private double lastX;
    private double lastY;
    private double orgSceneX;
    private double orgSceneY;
    private double orgTranslateX;
    private double orgTranslateY;

    private DragMoveResize(Region region) {
        this.region = region;
    }

    static void makeChangeable(Region region) {
        final DragMoveResize resizer = new DragMoveResize(region);

        region.setOnMousePressed(resizer::mousePressed);

        region.setOnMouseDragged(resizer::mouseDragged);

        region.setOnMouseMoved(resizer::mouseOver);

        region.setOnMouseReleased(event -> resizer.mouseReleased());

        region.setOnMouseExited(e -> {
            //TODO: use css id/classes instead of style tags.
            region.setStyle("-fx-border-width: none; "
                + "-fx-border-insets: none; -fx-effect: none;");
            region.setOpacity(0.3);
        });

        region.setOnMouseEntered(e -> region.setOpacity(0.85));
    }

    private void mousePressed(MouseEvent event) {
        lastX = event.getSceneX();
        lastY = event.getSceneY();

        if (getCorrectCursor(event) == null) {
            moving = true;
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            orgTranslateX = ((Region)(event.getSource())).getTranslateX();
            orgTranslateY = ((Region)(event.getSource())).getTranslateY();
        }

        moving = false;
        dragging = true;
    }

    private void mouseReleased() {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
        borderNone();
    }

    private void mouseOver(MouseEvent event) {
        // wenn cursor null ist, darf nicht vergroesstert / verkleinert werden
        usedCursor = getCorrectCursor(event);

        if (usedCursor != null || dragging) {
            region.setCursor(usedCursor);
            borderShadow();
        } else {
            region.setCursor(Cursor.DEFAULT);
            borderNone();
        }
    }

    private Cursor getCorrectCursor(MouseEvent event) {
        if (event.getY() < RESIZE_MARGIN) {
            if (event.getX() < RESIZE_MARGIN) {
                return Cursor.NW_RESIZE;
            }

            if (event.getX() > (region.getWidth() - RESIZE_MARGIN)) {
                return Cursor.NE_RESIZE;
            }

            return Cursor.N_RESIZE;
        }

        if (event.getY() > (region.getHeight() - RESIZE_MARGIN)) {
            if (event.getX() > (region.getWidth() - RESIZE_MARGIN)) {
                return Cursor.SE_RESIZE;
            }

            if (event.getX() < RESIZE_MARGIN) {
                return Cursor.SW_RESIZE;
            }

            return Cursor.S_RESIZE;
        }

        if (event.getX() > (region.getWidth() - RESIZE_MARGIN)) {
            return Cursor.E_RESIZE;
        }

        if (event.getX() < RESIZE_MARGIN) {
            return Cursor.W_RESIZE;
        }

        return null;
    }

    private void mouseDragged(MouseEvent event) {

        if (moving) {
            return;
        }

        if (!dragging) {
            return;
        }

        if (usedCursor == null) {
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((Region)(event.getSource())).setTranslateX(newTranslateX);
            ((Region)(event.getSource())).setTranslateY(newTranslateY);
        } else {
            if (usedCursor.equals(Cursor.N_RESIZE)) {
                region.setPrefHeight(region.getHeight() - event.getY());
            }

            if (usedCursor.equals(Cursor.NE_RESIZE)) {
                region.setPrefHeight(region.getHeight() - event.getY());
                region.setPrefWidth(event.getX());
            }

            if (usedCursor.equals(Cursor.NW_RESIZE)) {
                region.setPrefHeight(region.getHeight() - event.getY());
                region.setPrefWidth(region.getWidth() + (lastX - event.getSceneX()));
            }

            if (usedCursor.equals(Cursor.E_RESIZE)) {
                region.setPrefWidth(event.getX());
            }

            if (usedCursor.equals(Cursor.W_RESIZE)) {
                region.setPrefWidth(region.getWidth() + (lastX - event.getSceneX()));
            }

            if (usedCursor.equals(Cursor.S_RESIZE)) {
                region.setPrefHeight(region.getHeight() - (lastY - event.getSceneY()));
            }

            if (usedCursor.equals(Cursor.SE_RESIZE)) {
                region.setPrefHeight(region.getHeight() - (lastY - event.getSceneY()));
                region.setPrefWidth(event.getX());
            }

            if (usedCursor.equals(Cursor.SW_RESIZE)) {
                region.setPrefHeight(region.getHeight() - (lastY - event.getSceneY()));
                region.setPrefWidth(region.getWidth() + (lastX - event.getSceneX()));
            }
        }

        region.setOpacity(0.85);
        lastX = event.getSceneX();
        lastY = event.getSceneY();
    }

    private void borderShadow() {
        region.setStyle("-fx-border-width: 2; -fx-border-insets: 5;"
                + "-fx-effect: dropshadow(three-pass-box, derive(-fx-secondary, -20%), 10, 0, 0, 0);");
    }

    private void borderNone() {
        region.setStyle("-fx-border-width: none; -fx-border-insets: none; -fx-effect: none;");
    }
}
