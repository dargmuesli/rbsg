package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.PlayerTile;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InGameController {
    public static Logger logger = LogManager.getLogger();
    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, EnvironmentTile> environmentTiles = new HashMap<>();
    public static List<PlayerTile> playerTiles = new ArrayList<>();
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

    private final Pane selectionOverlay = new Pane();
    private StackPane lastSelectedPane;
    private Map<StackPane, Pane> overlayedStacks = new HashMap<>();
    private Map<String, StackPane> stackPaneMapByEnvironmentTileId = new HashMap<>();
    private Map<String, EnvironmentTile> environmentTileMapById = new HashMap<>();
    private Map<String, UnitTile> unitTileMapByTileId = new HashMap<>();
    private Map<String, String> previousTileMapById = new HashMap<>();
    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;

    public void initialize() {
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
                StackPane stack = new StackPane();
                stack.getChildren().addAll(TextureManager.computeTerrainTextureInstance(environmentTiles, j, i));
                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    StackPane eventStack = (StackPane) event.getSource();

                    if (lastSelectedPane == null) {
                        eventStack.getChildren().add(selectionOverlay);
                        lastSelectedPane = eventStack;
                    } else if (eventStack != lastSelectedPane) {
                        lastSelectedPane.getChildren().remove(selectionOverlay);
                        eventStack.getChildren().add(selectionOverlay);
                        lastSelectedPane = eventStack;
                    } else {
                        eventStack.getChildren().remove(selectionOverlay);
                        lastSelectedPane = null;
                    }

                    overlayedStacks.forEach((stackPane, pane) -> stackPane.getChildren().remove(pane));
                    overlayedStacks.clear();
                    previousTileMapById.clear();

                    if (lastSelectedPane != null) {
                        for (UnitTile unitTile : unitTiles) {
                            if (eventStack.equals(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()))) {
                                drawOverlay(environmentTileMapById.get(unitTile.getPosition()), unitTile.getMp());
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

        for (UnitTile unitTile : unitTiles) {
            unitTileMapByTileId.put(unitTile.getPosition(), unitTile);
            stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()).getChildren()
                .add(TextureManager.getTextureInstance(unitTile.getType()));
        }

        NotificationHandler.getInstance().sendSuccess("Game initialized.", logger);
    }

    private void drawOverlay(EnvironmentTile startTile, int mp) {
        if (mp == 0) {
            return;
        }

        UnitTile startUnitTile = unitTileMapByTileId.get(startTile.getId());

        Queue<Pair<EnvironmentTile, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(startTile, mp));

        while (!queue.isEmpty()) {
            Pair<EnvironmentTile, Integer> currentElement = queue.remove();
            EnvironmentTile currentTile = currentElement.getKey();
            Integer currentMp = currentElement.getValue();

            if (currentMp == 0) {
                return;
            }

            Arrays.asList(
                currentTile.getTop(),
                currentTile.getRight(),
                currentTile.getBottom(),
                currentTile.getLeft()).forEach((neighborId) -> {
                    if (neighborId == null || neighborId.equals(startTile.getId())) {
                        return;
                    }

                    EnvironmentTile neighborTile = environmentTileMapById.get(neighborId);
                    StackPane neighborStack = stackPaneMapByEnvironmentTileId.get(neighborId);

                    if (neighborTile.isPassable()
                        && !overlayedStacks.containsKey(neighborStack)) {

                        Pane overlay = new Pane();
                        UnitTile neighborUnitTile = unitTileMapByTileId.get(neighborId);

                        if (neighborUnitTile != null
                            && Arrays.asList(startUnitTile.getCanAttack()).contains(neighborUnitTile.getType())) {

                            overlay.getStyleClass().add("tile-attack");
                        } else {
                            //TODO: for when the gamelobby exists
                            // is it possible to have two units on the same field?
                            // is it possible to walk across a field on which a unit is already present?
                            overlay.getStyleClass().add("tile-path");
                        }

                        neighborStack.getChildren().add(overlay);
                        overlayedStacks.put(neighborStack, overlay);
                        previousTileMapById.put(neighborId, currentTile.getId());

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
