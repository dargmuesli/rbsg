package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameObject;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InGameController {

    public static InGameController instance;
    public static Logger logger = LogManager.getLogger();
    public static Map<Pair<Integer, Integer>, EnvironmentTile> environmentTiles = new HashMap<>();
    public static Map<String, InGameObject> inGameObjects = new HashMap<>();
    public static List<UnitTile> unitTiles = new ArrayList<>();
    public static boolean gameInitFinished = false;

    @FXML
    private AnchorPane errorContainer;
    @FXML
    private AnchorPane inGameScreen1;
    @FXML
    private AnchorPane inGameScreen;
    @FXML
    private AnchorPane leaveGame;
    @FXML
    private GridPane gameGrid;
    @FXML
    private HBox head;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnMiniMap;
    @FXML
    private JFXButton btnNo;
    @FXML
    private JFXButton btnYes;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private Pane miniMap;
    @FXML
    private AnchorPane turnUI;

    private final Pane selectionOverlay = new Pane();
    private StackPane lastSelectedPane;
    private Map<StackPane, Pane> overlayedStacks = new HashMap<>();
    private Map<String, StackPane> stackPaneMapByEnvironmentTileId = new HashMap<>();
    private Map<String, EnvironmentTile> environmentTileMapById = new HashMap<>();
    private Map<String, UnitTile> unitTileMapByTileId = new HashMap<>();
    private Map<String, String> previousTileMapById = new HashMap<>();
    private Map<UnitTile, Pane> unitPaneMapbyUnitTile = new HashMap<>();
    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;


    public static InGameController getInstance() {
        return instance;
    }

    public void moveUnit(String unitId, String newPos) {
        UnitTile currentUnit = null;
        for (UnitTile unit : unitTiles) {
            if (unitId.equals(unit.getId())) {
                currentUnit = unit;
                break;
            }
        }
        assert (currentUnit != null);
        UnitTile finalCurrentUnit = currentUnit;
        Platform.runLater(() -> {
            Pane texture = unitPaneMapbyUnitTile.get(finalCurrentUnit);
            stackPaneMapByEnvironmentTileId.get(finalCurrentUnit.getPosition()).getChildren()
                .remove(texture);
            stackPaneMapByEnvironmentTileId.get(newPos).getChildren().add(texture);
            finalCurrentUnit.setPosition(newPos);
        });
    }

    public void initialize() {
        instance = this;
        UserInterfaceUtils.initialize(
            inGameScreen, inGameScreen1, InGameController.class, btnFullscreen, errorContainer);

        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnMiniMap);


        fillGameGrid();

        miniMap = TextureManager.computeMinimap(environmentTiles, 100, 100, 5);
        miniMap.setVisible(false);
        inGameScreen.getChildren().add(miniMap);

        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/turnUI.fxml"));
        try {
            Parent parent = loader.load();
            TurnUiController controller = loader.getController();
            turnUI.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectionOverlay.setId("tile-selected");
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", inGameScreen);
            }
        } else if (event.getSource().equals(btnMiniMap)) {
            if (miniMap.isVisible()) {
                miniMap.setVisible(false);
            } else {
                miniMap.setVisible(true);
            }
        }
    }

    private void fillGameGrid() {
        int maxX = 0;
        int maxY = 0;
        int tryCounter = 0;

        while (!gameInitFinished) {
            try {
                Thread.sleep(1000);
                tryCounter++;
                if (tryCounter == 10) {
                    NotificationHandler.getInstance().sendError("The matchfield tiles couldn't be loaded.",
                        logger);
                    break;
                }

            } catch (InterruptedException e) {
                NotificationHandler.getInstance()
                    .sendError("Game couldn't be initialized!", logger, e);
            }
        }

        while (environmentTiles.get(new Pair<>(0, maxX)) != null) {
            maxX++;
        }

        while (environmentTiles.get(new Pair<>(maxY, 0)) != null) {
            maxY++;
        }

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {

                // Create stack panes with an environment texture for every game field.
                StackPane stack = new StackPane();
                stack.getChildren().addAll(TextureManager.computeTerrainTextureInstance(environmentTiles, j, i));

                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    StackPane eventStack = (StackPane) event.getSource();

                    if (!overlayedStacks.isEmpty() && overlayedStacks.containsKey(eventStack)) {
                        //find eviromenttile where you last klicked
                        EnvironmentTile source = null;
                        for (EnvironmentTile tile : environmentTiles.values()) {
                            if (eventStack.equals(stackPaneMapByEnvironmentTileId.get(tile.getId()))) {
                                source = tile;
                                break;
                            }
                        }
                        //find unittile where you clicked before
                        UnitTile previous = null;
                        for (UnitTile unitTile : unitTiles) {
                            if (lastSelectedPane.equals(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()))) {
                                previous = unitTile;
                                break;
                            }
                        }

                        LinkedList<String> path = new LinkedList<>();
                        path.addFirst(source.getId());
                        String next = previousTileMapById.get(source.getId());
                        assert previous != null;
                        while (!next.equals(previous.getPosition())) {
                            path.addFirst(next);
                            next = environmentTileMapById.get(previousTileMapById.get(next)).getId();
                        }

                        //server
                        GameSocket.instance.moveUnit(previous.getId(), path.toArray(new String[path.size()]));

                        //reset
                        lastSelectedPane.getChildren().remove(selectionOverlay);
                        lastSelectedPane = null;
                    } else {

                        if (lastSelectedPane == null) {
                            // Nothing was selected.
                            // The new field is selected.
                            eventStack.getChildren().add(selectionOverlay);
                            lastSelectedPane = eventStack;
                        } else if (eventStack != lastSelectedPane) {
                            // A new field was selected.
                            // The old field is deselected and the new field is selected.
                            lastSelectedPane.getChildren().remove(selectionOverlay);
                            eventStack.getChildren().add(selectionOverlay);
                            lastSelectedPane = eventStack;
                        } else {
                            // The old field was selected.
                            // The old field is deselected.
                            eventStack.getChildren().remove(selectionOverlay);
                            lastSelectedPane = null;
                        }
                    }

                    // All overlays and saved path parts are cleared.
                    overlayedStacks.forEach((stackPane, pane) -> stackPane.getChildren().remove(pane));
                    overlayedStacks.clear();
                    previousTileMapById.clear();

                    // Draw new path and attack overlays if a unit was selected.
                    // TODO: for when the gamelobby exists
                    //  Only allow this for own units.
                    if (lastSelectedPane != null) {
                        for (UnitTile unitTile : unitTiles) {
                            if (eventStack.equals(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()))) {
                                drawOverlay(environmentTileMapById.get(unitTile.getPosition()), unitTile.getMp());
                                // Do this only for the first (and hopefully only) unitTile.
                                break;
                            }
                        }
                    }
                });

                gameGrid.add(stack, j, i);
                stackPaneMapByEnvironmentTileId.put(environmentTiles.get(new Pair<>(j, i)).getId(), stack);
                environmentTileMapById.put(environmentTiles.get(new Pair<>(j, i)).getId(),
                    environmentTiles.get(new Pair<>(j, i)));
            }
        }

        // Add the unitTiles to a map and their texture to their game fields.
        for (UnitTile unitTile : unitTiles) {
            unitTileMapByTileId.put(unitTile.getPosition(), unitTile);
            Pane pane = TextureManager.getTextureInstance(unitTile.getType());
            stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()).getChildren()
                .add(pane);
            unitPaneMapbyUnitTile.put(unitTile, pane);
        }

        NotificationHandler.getInstance().sendSuccess("Game initialized.", logger);
    }

    private void drawOverlay(EnvironmentTile startTile, int mp) {
        UnitTile startUnitTile = unitTileMapByTileId.get(startTile.getId());

        // Create a queue for breadth search.
        Queue<Pair<EnvironmentTile, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(startTile, mp));

        while (!queue.isEmpty()) {

            // Take the first element from the queue.
            Pair<EnvironmentTile, Integer> currentElement = queue.remove();
            EnvironmentTile currentTile = currentElement.getKey();
            Integer currentMp = currentElement.getValue();

            // Limit moving distance.
            if (currentMp == 0) {
                return;
            }

            // Calculate overlays for all four neighbors.
            Arrays.asList(
                currentTile.getTop(),
                currentTile.getRight(),
                currentTile.getBottom(),
                currentTile.getLeft()).forEach((neighborId) -> {

                    // Limit to existing fields and exclude the selected tile.
                    if (neighborId == null || neighborId.equals(startTile.getId())) {
                        return;
                    }

                    EnvironmentTile neighborTile = environmentTileMapById.get(neighborId);
                    StackPane neighborStack = stackPaneMapByEnvironmentTileId.get(neighborId);

                    // Exclude tiles that cannot be passed and skip tiles that already received an overlay.
                    if (neighborTile.isPassable()
                        && !overlayedStacks.containsKey(neighborStack)) {

                        Pane overlay = new Pane();
                        UnitTile neighborUnitTile = unitTileMapByTileId.get(neighborId);

                        if (neighborUnitTile != null
                            && Arrays.asList(startUnitTile.getCanAttack()).contains(neighborUnitTile.getType())) {

                            // The tile that is going to receive an overlay contains a unit that can be attacked.
                            // TODO: for when the gamelobby exists
                            //  Only allow this for own units.
                            overlay.getStyleClass().add("tile-attack");
                        } else {
                            // TODO: for when the gamelobby exists
                            //  Is it possible to have two units on the same field?
                            //  Is it possible to walk across a field on which a unit is already present?
                            overlay.getStyleClass().add("tile-path");
                        }

                        // Add the overlay to the tile and a map so that it can easily be removed in the future.
                        neighborStack.getChildren().add(overlay);
                        overlayedStacks.put(neighborStack, overlay);

                        // Save the tile from which the tile that received an overlay was reached so that a path can
                        // be reconstructed for server requests.
                        previousTileMapById.put(neighborId, currentTile.getId());

                        // Add the tile that received an overlay to the quere so that its neighbors are checked too.
                        queue.add(new Pair<>(neighborTile, currentMp - 1));
                    }
                });
        }
    }


    public void leaveGame(ActionEvent event) {

        if (event.getSource().equals(btnBack)) {
            leaveGame.setLayoutX(head.getWidth() - leaveGame.getWidth());
            leaveGame.setLayoutY(head.getHeight());
            leaveGame.setVisible(true);
            for (Node node : leaveGame.getChildren()) {
                node.setVisible(true);
            }

        } else if (event.getSource().equals(btnYes)) {
            GameSocket.instance.leaveGame();
            GameSocket.instance.disconnect();
            MainController.setInGameChat(false);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", inGameScreen);
        } else if (event.getSource().equals(btnNo)) {
            leaveGame.setVisible(false);
            for (Node node : leaveGame.getChildren()) {
                node.setVisible(false);
            }
        }

    }
}