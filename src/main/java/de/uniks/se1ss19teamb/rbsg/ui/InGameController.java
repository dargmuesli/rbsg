package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXToggleButton;
import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.bot.BotControl;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameObject;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.ui.modules.TurnUiController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.ThreadLocks;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InGameController {

    private static final double ZOOM_FACTOR = 0.07;
    public static InGameController instance;
    public static Logger logger = LogManager.getLogger();
    public static Map<Pair<Integer, Integer>, EnvironmentTile> environmentTiles = new HashMap<>();
    public static Map<String, EnvironmentTile> environmentTileMapById = new HashMap<>();
    public static Map<String, InGameObject> inGameObjects = new HashMap<>();
    public static Map<String, UnitTile> movedUnitTiles = new HashMap<>();
    public static Map<String, String> previousTileMapById = new HashMap<>();
    public static Map<String, String> previousTileAttackMapById = new HashMap<>();
    public static Map<String, UnitTile> unitTileMapByTileId = new HashMap<>();
    public static List<UnitTile> unitTiles = new ArrayList<>();
    public static Map<String, StackPane> stackPaneMapByEnvironmentTileId = new HashMap<>();

    @FXML
    private AnchorPane apnRoot;
    @FXML
    private AnchorPane apnFade;
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
    private JFXButton btnNo;
    @FXML
    private JFXButton btnYes;
    @FXML
    private ScrollPane mapScrollPane;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private Pane miniMap;
    @FXML
    private AnchorPane turnUI;
    @FXML
    public AnchorPane winScreenPane;
    @FXML
    public VBox vBoxWinScreen;
    @FXML
    public JFXToggleButton autoMode;

    private final Pane selectionOverlay = new Pane();
    private StackPane lastSelectedPane;
    private Map<StackPane, Pane> overlayedStacks = new HashMap<>();
    private int zoomCounter = 0;
    private Map<UnitTile, Pane> unitPaneMapbyUnitTile = new HashMap<>();
    private AI aI = null;

    private String playerId;
    private boolean logout;
    private UnitTile unitHealth = new UnitTile();
    private ArrayList<Pane> unitPanes = new ArrayList<>();

    public static InGameController getInstance() {
        return instance;
    }

    /**
     * Removes the unit from its current position on the map and readds it at the new position.
     *
     * @param unitId The unit to move.
     * @param newPos The position to move to.
     */
    public void changeUnitPos(String unitId, String newPos) {
        UnitTile currentUnit = null;

        for (UnitTile unit : unitTiles) {
            if (unitId.equals(unit.getId())) {
                currentUnit = unit;
                break;
            }
        }

        assert (currentUnit != null);

        UnitTile finalCurrentUnit = currentUnit;
        unitTileMapByTileId.remove(currentUnit.getPosition());

        Platform.runLater(() -> {
            Pane texture = unitPaneMapbyUnitTile.get(finalCurrentUnit);
            stackPaneMapByEnvironmentTileId.get(finalCurrentUnit.getPosition()).getChildren()
                .remove(texture);
            if (miniMap.isVisible()) {
                updateMinimap();
            }

            if (newPos != null) { // delete UnitTile if no given position
                stackPaneMapByEnvironmentTileId.get(newPos).getChildren().add(texture);
            }
        });

        if (newPos != null) {
            unitTileMapByTileId.put(newPos, currentUnit);
            SoundManager.playSound(
                finalCurrentUnit.getType().replaceAll(" ", "") + "_Move", 0);
            currentUnit.setPosition(newPos);
        }
    }

    private void updateMinimap() {
        miniMap.getChildren().clear();
        miniMap.getChildren().add(TextureManager.computeMinimap(environmentTiles, -1, unitTileMapByTileId));
    }

    @FXML
    private void initialize() {
        instance = this;
        UserInterfaceUtils.initialize(apnFade, apnRoot, InGameController.class, btnFullscreen);

        for (Node node : head.getChildren()) {
            if (node.getClass().equals(JFXButton.class)) {
                Theming.hamburgerMenuTransition(hamburgerMenu, (JFXButton) node);
            }
        }

        fillGameGrid();

        // changing width and height to heigher values makes the canvas of the minimap too big
        // if you want to change the size of minimap please use the size parameter (or rework calculation)
        miniMap.getChildren().add(TextureManager.computeMinimap(environmentTiles, -1, unitTileMapByTileId));
        miniMap.setVisible(false);
        apnFade.getChildren().add(miniMap);

        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/modules/turnUI.fxml"));
        try {
            Parent parent = loader.load();
            turnUI.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectionOverlay.setId("tile-selected");

        FXMLLoader loader1 = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/modules/winScreen.fxml"));
        try {
            Parent parent = loader1.load();
            winScreenPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BotControl.initializeBotAi(instance);
    }

    @FXML
    private void bigger() {
        zoomCounter++;
        zoom();
    }

    @FXML
    private void logout() {
        logout = true;
        askConcede();
    }

    @FXML
    private void smaller() {
        zoomCounter--;
        zoom();
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    private void toggleMinimap() {
        if (miniMap.isVisible()) {
            miniMap.setVisible(false);
        } else {
            updateMinimap();
            miniMap.setVisible(true);
        }
    }

    private void zoom() {

        Scale scale = new Scale(1 + zoomCounter * ZOOM_FACTOR, 1 + zoomCounter * ZOOM_FACTOR, 0, 0);

        if (mapScrollPane.getHeight() < gameGrid.getHeight() * (scale.getY() + ZOOM_FACTOR)) {
            Group zoomGroup = new Group();
            zoomGroup.getChildren().add(gameGrid);
            Group contentGroup = new Group();
            zoomGroup.getTransforms().add(scale);
            contentGroup.getChildren().add(zoomGroup);
            mapScrollPane.setContent(contentGroup);
        } else {
            zoomCounter++;
        }
    }

    /**
     * Runs an AI bot.
     */
    @FXML
    public void autoMode() {
        if (autoMode.isSelected()) {
            String userName = LoginController.getUserName();

            outer:
            for (InGamePlayer player : TurnUiController.getInstance().inGamePlayerList) {
                if (userName.equals(player.getName())) {
                    playerId = player.getId();
                    break;
                } else {
                    for (BotUser botUser : BotControl.botUsers) {
                        if (botUser.getBotUserName().equals(player.getName())) {
                            playerId = player.getId();
                            break outer;
                        }
                    }
                }
            }

            assert playerId != null;

            if (aI == null) {
                aI = AI.instantiate(Integer.MAX_VALUE);
                aI.initialize(playerId, GameSocketDistributor.getGameSocket(0), InGameController.instance);
            }

            if (Objects.requireNonNull(GameSocketDistributor.getGameSocket(0)).currentPlayer.equals(playerId)) {
                if (!Objects.requireNonNull(GameSocketDistributor.getGameSocket(0))
                    .phaseString.equals("Movement Phase")) {
                    autoMode.setSelected(false);
                    NotificationHandler.sendWarning("You can only activate Automode\nin your first Movementphase\n"
                            + "or on your opponents turn.", logger);
                } else {
                    Objects.requireNonNull(aI).doTurn();
                }
            }
        }
    }

    private void fillGameGrid() {
        int maxX = 0;
        int maxY = 0;
        int tryCounter = 0;

        while (!GameLobbyController.instance.gameInitFinished) {
            try {
                Thread.sleep(1000);
                tryCounter++;
                if (tryCounter == 10) {
                    NotificationHandler.sendError("The matchfield tiles couldn't be loaded.",
                        logger);
                    break;
                }

            } catch (InterruptedException e) {
                NotificationHandler.sendError("Game couldn't be initialized!", logger, e);
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
                        // Find last clicked eviroment tile.
                        EnvironmentTile source = null;

                        for (EnvironmentTile tile : environmentTiles.values()) {
                            if (eventStack.equals(stackPaneMapByEnvironmentTileId.get(tile.getId()))) {
                                source = tile;
                                break;
                            }
                        }

                        // Find last clicked unit tile.
                        UnitTile previousUnitTile = null;

                        for (UnitTile unitTile : unitTiles) {
                            if (lastSelectedPane.equals(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()))) {
                                previousUnitTile = unitTile;
                                break;
                            }
                        }

                        if (previousUnitTile != null && movedUnitTiles.containsKey(previousUnitTile.getId())) {
                            previousUnitTile = movedUnitTiles.get(previousUnitTile.getId());
                        }

                        assert previousUnitTile != null;
                        assert source != null;

                        UnitTile toAttack = unitTileMapByTileId.get(source.getId());
                        EnvironmentTile lastSelected = environmentTileMapById.get(previousUnitTile.getPosition());
                        assert lastSelected != null;

                        // Is there a unit on the selected field and is the selected field a neighbor?
                        if (toAttack != null
                            && ((lastSelected.getBottom() != null && lastSelected.getBottom().equals(source.getId()))
                            || (lastSelected.getLeft() != null && lastSelected.getLeft().equals(source.getId()))
                            || (lastSelected.getRight() != null && lastSelected.getRight().equals(source.getId()))
                            || (lastSelected.getTop() != null && lastSelected.getTop().equals(source.getId())))
                        ) {
                            // Yes: attack.
                            Objects.requireNonNull(GameSocketDistributor.getGameSocket(0))
                                .attackUnit(previousUnitTile.getId(), toAttack.getId());

                        } else {
                            // No: move.
                            LinkedList<String> path = new LinkedList<>();
                            path.addFirst(source.getId());
                            String next = previousTileMapById.get(source.getId());
                            int moveDistance = 1;

                            while (!next.equals(previousUnitTile.getPosition())) {
                                path.addFirst(next);
                                next = environmentTileMapById.get(previousTileMapById.get(next)).getId();
                                moveDistance++;
                            }

                            Objects.requireNonNull(GameSocketDistributor.getGameSocket(0))
                                .moveUnit(previousUnitTile.getId(), path.toArray(new String[0]));

                            UnitTile movedUnitTile = new UnitTile(previousUnitTile);
                            movedUnitTile.setMp(movedUnitTile.getMp() - moveDistance);
                            movedUnitTile.setPosition(source.getId());
                            movedUnitTiles.put(movedUnitTile.getId(), movedUnitTile);
                        }

                        // Remove selection overlay and reset selected pane variable.
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
                    ThreadLocks.getWriteLockPreviousTileMapById().lock();
                    previousTileMapById.clear();
                    ThreadLocks.getWriteLockPreviousTileMapById().unlock();

                    // Draw new path and attack overlays if a unit was selected.
                    // TODO: for when the gamelobby exists
                    //  Only allow this for own units.
                    if (lastSelectedPane != null) {
                        for (UnitTile unitTile : unitTiles) {
                            if (eventStack.equals(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()))) {
                                drawOverlay(environmentTileMapById.get(unitTile.getPosition()),
                                    movedUnitTiles.containsKey(unitTile.getId())
                                        ? movedUnitTiles.get(unitTile.getId()).getMp()
                                        : unitTile.getMp());
                                // Do this only for the first (and hopefully only) unitTile.
                                break;
                            }
                        }
                    }
                });

                gameGrid.add(stack, j, i);
                stackPaneMapByEnvironmentTileId.put(environmentTiles.get(new Pair<>(j, i)).getId(), stack);
                ThreadLocks.getWriteEnvironmentTileMapById().lock();
                environmentTileMapById.put(environmentTiles.get(new Pair<>(j, i)).getId(),
                    environmentTiles.get(new Pair<>(j, i)));
                ThreadLocks.getWriteEnvironmentTileMapById().unlock();
            }
        }

        // Add the unitTiles to a map and their texture to their game fields.
        for (UnitTile unitTile : unitTiles) {
            unitTileMapByTileId.put(unitTile.getPosition(), unitTile);
            //adds player color to unitpane and adds healthBar
            Pane unitPane = new Pane();
            unitPanes.add(unitPane);

            unitPane.getChildren().add(generateUnitPane(unitTile));
            unitPane.getChildren().add(TextureManager.getTextureInstanceWithSize("HealthBarBorder", 6, 50));
            unitPane.getChildren().add(TextureManager.getTextureInstanceWithSize("HealthBarBackground", 6, 50));
            unitPane.getChildren().add(TextureManager.getTextureInstanceWithSize("HealthBarForeground", 6, 50));

            for (int i = 1; i < unitPane.getChildren().size(); i++) {
                unitPane.getChildren().get(i).setLayoutY(55);
            }

            stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()).getChildren()
                .add(unitPane);
            unitPaneMapbyUnitTile.put(unitTile, unitPane);

        }

        NotificationHandler.sendSuccess("Game initialized.", logger);
    }

    private Pane generateUnitPane(UnitTile unitTile) {
        InGamePlayer player = (InGamePlayer) inGameObjects.get(unitTile.getLeader());

        return TextureManager.getTextureInstance(unitTile.getType(),
            (player != null) ? player.getColor() : null);
    }

    public void drawOverlay(EnvironmentTile startTile, int mp) {
        drawOverlay(startTile, mp, true, LoginController.getUserName());
    }

    /**
     * Draws an overlay onto the map, showing tiles to possibly move to and to attack.
     *
     * @param startTile The tile from which the paths are calculated.
     * @param mp        The available movement points.
     * @param draw      Indicates whether the overlay should be actually drawn or just calculated.
     * @param playerId  The id of the player currently interacts.
     */
    public void drawOverlay(EnvironmentTile startTile, int mp, boolean draw, String playerId) {
        UnitTile startUnitTile = unitTileMapByTileId.get(startTile.getId());
        ThreadLocks.getWriteLockPreviousTileMapById().lock();
        previousTileMapById.clear();
        ThreadLocks.getWriteLockPreviousTileMapById().unlock();
        previousTileAttackMapById.clear();

        // Create a queue for breadth search.
        Queue<Pair<EnvironmentTile, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(startTile, mp));

        while (!queue.isEmpty()) {

            // Take the first element from the queue.
            Pair<EnvironmentTile, Integer> currentElement = queue.remove();
            EnvironmentTile currentTile = currentElement.getKey();
            Integer currentMp = currentElement.getValue();

            // Limit moving distance.
            // currentMp = 0 -> Attackable but not moveable
            if (currentMp == -1) {
                return;
            }

            // Calculate overlays for all four neighbors.
            Arrays.asList(
                currentTile.getTop(),
                currentTile.getRight(),
                currentTile.getBottom(),
                currentTile.getLeft()).forEach((neighborId) -> {

                    // Limit to existing fields that haven't been checked yet and exclude the selected tile.

                    if (neighborId == null || neighborId.equals(startTile.getId())
                        || previousTileMapById.containsKey(neighborId)
                        || previousTileAttackMapById.containsKey(neighborId)) {

                        return;
                    }

                    EnvironmentTile neighborTile = environmentTileMapById.get(neighborId);
                    StackPane neighborStack = stackPaneMapByEnvironmentTileId.get(neighborId);

                    // Exclude tiles that cannot be passed and skip tiles that already received an overlay.
                    // Skip tiles with own units
                    if (neighborTile.isPassable()
                        && !overlayedStacks.containsKey(neighborStack)
                        && (unitTileMapByTileId.get(neighborId) == null

                        || !((InGamePlayer) inGameObjects.get(unitTileMapByTileId.get(neighborId).getLeader()))
                        .getName().equals(playerId))) {

                        Pane overlay = new Pane();
                        UnitTile neighborUnitTile = unitTileMapByTileId.get(neighborId);

                        if (neighborUnitTile != null
                            && Arrays.asList(startUnitTile.getCanAttack()).contains(neighborUnitTile.getType())) {

                            // The tile that is going to receive an overlay contains a unit that can be attacked.
                            // TODO: for when the gamelobby exists
                            //  Only allow this for own units.
                            overlay.getStyleClass().add("tile-attack");

                            previousTileAttackMapById.put(neighborId, currentTile.getId());
                        } else if (currentMp > 0 && neighborUnitTile == null) {
                            // currentMp = 0 -> Attackable but not moveable
                            // TODO: for when the gamelobby exists
                            //  Is it possible to have two units on the same field?
                            //  Is it possible to walk across a field on which a unit is already present?
                            overlay.getStyleClass().add("tile-path");

                            // Save the tile from which the tile that received an overlay was reached so that a path can
                            // be reconstructed for server requests. But only if it's a move not an attack tile
                            ThreadLocks.getWriteLockPreviousTileMapById().lock();
                            previousTileMapById.put(neighborId, currentTile.getId());
                            ThreadLocks.getWriteLockPreviousTileMapById().unlock();
                            
                            // Add the tile that received an overlay to the quere so that its neighbors are checked too.
                            // But only if movement, as we can't pass through attackable units
                            queue.add(new Pair<>(neighborTile, currentMp - 1));
                        }

                        // Add the overlay to the tile and a map so that it can easily be removed in the future.
                        if (draw) {
                            neighborStack.getChildren().add(overlay);
                            overlayedStacks.put(neighborStack, overlay);
                        }
                    }
                });
        }
    }

    /**
     * (Optionally) leaves the game, differentiating between three ways: click on "back", on "concede &gt; yes"
     * or "concede &gt; no".
     *
     * @param event The click's source.
     */
    public void leaveGame(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            logout = false;
            askConcede();
        } else if (event.getSource().equals(btnYes)) {
            Objects.requireNonNull(GameSocketDistributor.getGameSocket(0)).leaveGame();
            Objects.requireNonNull(GameSocketDistributor.getGameSocket(0)).disconnect();
            if (logout) {
                UserInterfaceUtils.logout(apnFade, btnLogout);
            } else {
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", apnFade);
            }
        } else if (event.getSource().equals(btnNo)) {
            leaveGame.setVisible(false);
            for (Node node : leaveGame.getChildren()) {
                node.setVisible(false);
            }
        }
    }

    private void askConcede() {
        leaveGame.setLayoutX(head.getWidth() - leaveGame.getWidth());
        leaveGame.setLayoutY(head.getHeight());
        leaveGame.setVisible(true);
        for (Node node : leaveGame.getChildren()) {
            node.setVisible(true);
        }
    }

    /**
     * Updates the health points of a unit.
     *
     * @param unitId The id of the unit that is to be updated.
     * @param newHp  The health points that are to be set.
     */
    public void changeUnitHp(String unitId, String newHp) {
        UnitTile unit = null;

        for (UnitTile unitTile : unitTiles) {
            if (unitTile.getId().equals(unitId)) {
                unit = unitTile;
                break;
            }
        }

        assert unit != null;
        unit.setHp(Integer.parseInt(newHp));
        setUnitHealth(unit.getHp());

        //for sounds find the attacking unit
        UnitTile attacker = findAttackingUnit(unit);

        if (attacker != null) {
            SoundManager.playSound(attacker.getType().replaceAll(" ", ""), 0);
        }

        updateHealth(unitId);
    }

    /**
     * Returns the attacking unit by checking the field's neighbors.
     *
     * @param unit The unit that is attacked.
     * @return     The unit that attacks.
     */
    public UnitTile findAttackingUnit(UnitTile unit) {
        UnitTile neighbor = null;
        EnvironmentTile unitPos = environmentTileMapById.get(unit.getPosition());

        for (UnitTile unitTile : unitTiles) {
            if ((unitPos.getBottom() != null && unitPos.getBottom().equals(unitTile.getPosition()))
                || (unitPos.getLeft() != null && unitPos.getLeft().equals(unitTile.getPosition()))
                || (unitPos.getRight() != null && unitPos.getRight().equals(unitTile.getPosition()))
                || (unitPos.getTop() != null && unitPos.getTop().equals(unitTile.getPosition()))) {
                neighbor = unitTile;
                break;
            }
        }

        return neighbor;
    }

    private void setUnitHealth(int health) {
        unitHealth.setHp(health);
    }

    private int getUnitHealth() {
        return unitHealth.getHp();
    }

    private void updateHealth(String id) {
        Platform.runLater(() -> {
            for (int i = 0; i < unitTiles.size(); i++) {
                if (unitTiles.get(i) != null && unitTiles.get(i).getId().equals(id)) {
                    unitPanes.get(i).getChildren().remove(3);
                    unitPanes.get(i).getChildren().add(TextureManager.getTextureInstanceWithSize("HealthBarForeground",
                        6, getUnitHealth() * 5));
                    unitPanes.get(i).getChildren().get(3).setLayoutY(55);
                }
            }
        });

    }
}
