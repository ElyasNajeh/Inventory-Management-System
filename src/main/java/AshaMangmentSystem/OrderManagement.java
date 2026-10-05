package AshaMangmentSystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class OrderManagement {
	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderManagement.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

}
