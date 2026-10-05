package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class CustomerManagement {
    @FXML
    private Button cancel;
    @FXML
    private TextField search;
    public BorderPane main() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerManagement.fxml"));
            loader.setController(this);
            BorderPane root = loader.load();

            Styling.setSmallButtonsStyle(cancel);
            Styling.setSearchTxtStyle(search);
			cancel.setOnAction(event -> search.clear());

            return root;
        } catch (IOException e) {
            MyAlert.uiError(e);
            return null;
        }
    }
}
