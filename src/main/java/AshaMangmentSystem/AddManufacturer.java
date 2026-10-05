package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddManufacturer {
	@FXML
	private Label title;
	@FXML
	private TextField name;
	@FXML
	private TextField location;
	@FXML
	private TextField email;
	@FXML
	private Button cancel;
	@FXML
	private Button action;

	public void addManufacturer() {
		String nameManu = name.getText().trim();
		String locationManu = location.getText().trim();
		String emailManu = email.getText().trim().toLowerCase();
		for (Manufacturer manufacturer : Catalog.getManufacturerList()) {
			if (emailManu.equals(manufacturer.getEmail())) {
				MyAlert.alert("Error", "This email already exists", AlertType.ERROR);
				return;
			}
		}
		try {
			new Manufacturer(nameManu, locationManu, emailManu);
		} catch (IllegalArgumentException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
			return;
		}
		boolean confirmation = MyAlert.alert("Confirmation", "Are you sure you need to add this Manufacturer",
				AlertType.CONFIRMATION);
		if (!confirmation) {
			return;
		}

		String sql = "INSERT INTO Manufacturer (manufacturer_name, manufacturer_location,manufacturer_email) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nameManu);
			stmt.setString(2, locationManu);
			stmt.setString(3, emailManu);
			stmt.executeUpdate();
			MyAlert.alert("Success", "Manufacturer added successfully", AlertType.INFORMATION);
			name.clear();
			location.clear();
			email.clear();

		} catch (Exception e) {
			MyAlert.databaseError(e);
		}
	}

	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddManufacturer.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();
			Styling.setTitlesStyle(title);
			actions();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	private void actions() {
		action.setOnAction(e -> addManufacturer());
		cancel.setOnAction(e -> Main.setMain(new ProductManagement().main()));
	}
}
