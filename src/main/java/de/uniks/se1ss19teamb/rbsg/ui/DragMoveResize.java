package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class DragMoveResize {

    private static final int RESIZE_MARGIN = 5;

    private final Region region;

    private double yvalue;
    private double xvalue;

    private boolean initMinHeight;
    private boolean initMinWidth;
    private boolean moving;
    private boolean dragging;

    private double lastX;
    private double lastY;

    private Cursor usedCursor;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    private DragMoveResize(Region region) {
        this.region = region;
    }

    public static void makeChangeable(Region region) {
        final DragMoveResize resizer = new DragMoveResize(region);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }
        });

        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }
        });

        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }
        });

        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    private void mousePressed(MouseEvent event) {
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

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        // wenn cursor null ist, darf nicht vergroesstert / verkleinert werden
        usedCursor = getCorrectCursor(event);

        if (usedCursor != null || dragging) {
            region.setCursor(usedCursor);
        } else {
            region.setCursor(Cursor.DEFAULT);
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

        if (event.getX() > (region.getWidth() - RESIZE_MARGIN)) {
            return Cursor.E_RESIZE;
        }

        if (event.getX() < RESIZE_MARGIN) {
            return Cursor.W_RESIZE;
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

        return null;
    }



    protected void mouseDragged(MouseEvent event) {

        if (moving) {
            return;
        }

        if (!dragging) {
            return;
        }

        if (usedCursor != null && usedCursor.equals(Cursor.N_RESIZE)) {
            region.setPrefHeight(region.getHeight() - event.getY());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.NE_RESIZE)) {
            region.setPrefHeight(region.getHeight() - event.getY());
            region.setPrefWidth(event.getX());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.NW_RESIZE)) {
            region.setPrefHeight(region.getHeight() - event.getY());
            region.setPrefWidth(region.getWidth() - event.getX());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.E_RESIZE)) {
            region.setPrefWidth(event.getX());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.W_RESIZE)) {
            region.setPrefWidth(region.getWidth() - event.getX());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.S_RESIZE)) {
            region.setPrefHeight(event.getY());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.SE_RESIZE)) {
            region.setPrefHeight(event.getY());
            region.setPrefWidth(event.getX());
        }

        if (usedCursor != null && usedCursor.equals(Cursor.SW_RESIZE)) {
            region.setPrefHeight(event.getY());
            region.setPrefWidth(region.getWidth() - event.getX());
        }

        if (usedCursor == null) {
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((Region)(event.getSource())).setTranslateX(newTranslateX);
            ((Region)(event.getSource())).setTranslateY(newTranslateY);
        }
    }
}
