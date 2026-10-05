package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class AddPayment {
    @FXML
    private Label title;
	@FXML
	private Button cancel;
	@FXML
	private Button action;

    public BorderPane main() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPayment.fxml"));
            loader.setController(this);
            BorderPane root = loader.load();
            Styling.setTitlesStyle(title);
			action.setDisable(true);
			action.setText("Not available");
			cancel.setOnAction(event -> Main.setMain(new ProductManagement().main()));
            return root;

        } catch (IOException e) {
            MyAlert.uiError(e);
            return null;
        }
    }
}
