package boardgame;

import boardgame.model.TicTacToeState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardGameController {
    @FXML
    private GridPane board;

    @FXML
    private Button newGameButton;

    @FXML
    private Button finishGameButton;

    private BooleanProperty inGame;
    private ObjectProperty<TicTacToeState> model;

    @FXML
    private void initialize() {
        inGame = new SimpleBooleanProperty(false);
        model = new SimpleObjectProperty<>(new TicTacToeState());
        inGame.addListener((element, old, actual) -> {
            newGameButton.setDisable(actual);
            finishGameButton.setDisable(!actual);
        });
        finishGameButton.setDisable(true);
    }
}
