package AshaMangmentSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class UpdateManufacturer {
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

	Manufacturer manufacturer;

	public UpdateManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void loadIndex(Manufacturer manufacturer) {
		if (manufacturer != null) {
			name.setText(manufacturer.getName());
			location.setText(manufacturer.getLocation());
			email.setText(manufacturer.getEmail());
		}
	}

	public void updateButton() {
		String nameManu = name.getText().trim();
		String locationManu = location.getText().trim();
		String emailManu = email.getText().trim().toLowerCase();
		try {
			new Manufacturer(nameManu, locationManu, emailManu);
		} catch (IllegalArgumentException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
			return;
		}
		boolean confirmation = MyAlert.alert("Confirmation", "Are you sure you need to Update this Manufacturer ?",
				AlertType.CONFIRMATION);
		if (!confirmation) {
			return;
		}
		String sql = "UPDATE manufacturer SET manufacturer_name = ?, manufacturer_location = ?, manufacturer_email = ? WHERE manufacturer_id = ?";
		try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nameManu);
			stmt.setString(2, locationManu);
			stmt.setString(3, emailManu);
			stmt.setString(4, manufacturer.getId());

			stmt.executeUpdate();
			manufacturer.setName(nameManu);
			manufacturer.setLocation(locationManu);
			manufacturer.setEmail(emailManu);

			MyAlert.alert("Success", "Manufacturer Updated Successfully ", AlertType.INFORMATION);
		} catch (Exception e) {
			MyAlert.databaseError(e);
		}
	}

	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateManufacturer.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();
			Styling.setTitlesStyle(title);
			loadIndex(manufacturer);
			actions();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	private void actions() {
		action.setOnAction(e -> updateButton());
		cancel.setOnAction(e -> Main.setMain(new ManufacturerManagement().main()));
	}
}
