package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class AddCustomer {
    @FXML
    private Label title;
    @FXML
    private TextField name;
    @FXML
    private TextField unitPrice;
    @FXML
    private VBox phoneNumbersContainer;
    @FXML
    private Button addPhoneButton;
    @FXML
    private Button cancel;
    @FXML
    private Button action;

    public BorderPane main() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
            loader.setController(this);
            BorderPane root = loader.load();
            setupActions();
            Styling.setAddPhoneNumberButtonStyle(addPhoneButton);
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

    private void setupActions() {
        addPhoneButton.setOnAction(event -> {
            TextField newPhoneField = new TextField();
            phoneNumbersContainer.getChildren().add(newPhoneField);
        });
    }

}
