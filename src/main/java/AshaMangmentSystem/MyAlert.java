package AshaMangmentSystem;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class MyAlert {
	private MyAlert() {
	}

	public static boolean alert(String head, String message, Alert.AlertType type) { // a method to show normal alert
		Alert alert = new Alert(type);
		alert.setTitle(head);
		alert.setContentText(message);
		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}

	public static void databaseError(Exception exception) {
		String message = exception.getMessage();
		alert("Database Error", message == null || message.isBlank() ? "The database operation failed." : message,
				Alert.AlertType.ERROR);
	}

	public static void uiError(Exception exception) {
		String message = exception.getMessage();
		alert("UI Error", message == null || message.isBlank() ? "The requested screen could not be loaded." : message,
				Alert.AlertType.ERROR);
	}
}
