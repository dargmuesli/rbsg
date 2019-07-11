package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class GameLobbyController {
    @FXML
    private AnchorPane gameLobby1;

    @FXML
    private AnchorPane gameLobby;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXButton btnFullscreen;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Label labelGameName;

    @FXML
    private VBox playerList;

    @FXML
    private Label labelMorePlayer;

    @FXML
    private JFXButton btnReady1;



    public void initialize(){
        /*
        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby,gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu,btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu,btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu,btnFullscreen);


        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        UserInterfaceUtils.makeFadeInTransition(gameLobby);

         */
    }



}
