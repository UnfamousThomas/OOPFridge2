package ut.delta.oopkulmkapp2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KappController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Tere tulemast külmkapi haldamise programmi kasutama!");
    }
}