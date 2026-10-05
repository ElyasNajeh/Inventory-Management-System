package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

public class NewShipment {
	@FXML
	private TableView<Entry> entryTable;

	@FXML
	private TableColumn<Entry, String> colProductId;

	@FXML
	private TableColumn<Entry, String> colProductName;

	@FXML
	private TableColumn<Entry, Integer> colAmount;

	@FXML
	private TableColumn<Entry, String> colStartDate;

	@FXML
	private TableColumn<Entry, String> colExpiryDate;

	@FXML
	private Button removeButton;

	@FXML
	private Button addProductButton;

	@FXML
	private Button confirmButton;

	@FXML
	private ComboBox<Manufacturer> manufacturerComboBox;

	@FXML
	private DatePicker shipmentDatePicker;

	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewShipment.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();
			manufacturerComboBox.setItems(Catalog.getManufacturerList());

			colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
			colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
			colAmount.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
			colExpiryDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
			entryTable.setItems(EntryManagement.entryListTemp);

			actions();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	private int addShipment(Manufacturer manufacturer, LocalDate shipmentDate) throws SQLException {
		String shipmentSql = "INSERT INTO shipment (manufacturer_id, shipment_date) VALUES (?, ?)";
		String entrySql = "INSERT INTO entry (start_date, expiry_date, quantity, shipment_id, product_id) "
				+ "VALUES (?, ?, ?, ?, ?)";
		String quantitySql = "UPDATE product SET total_quantity = total_quantity + ? WHERE product_id = ?";

		try (Connection conn = DBConnection.connect()) {
			conn.setAutoCommit(false);
			try (PreparedStatement shipmentStatement = conn.prepareStatement(shipmentSql,
					Statement.RETURN_GENERATED_KEYS);
					PreparedStatement entryStatement = conn.prepareStatement(entrySql);
					PreparedStatement quantityStatement = conn.prepareStatement(quantitySql)) {
				shipmentStatement.setString(1, manufacturer.getId());
				shipmentStatement.setDate(2, java.sql.Date.valueOf(shipmentDate));
				shipmentStatement.executeUpdate();

				int shipmentId;
				try (ResultSet generatedKeys = shipmentStatement.getGeneratedKeys()) {
					if (!generatedKeys.next()) {
						throw new SQLException("The database did not return a shipment ID.");
					}
					shipmentId = generatedKeys.getInt(1);
				}

				for (Entry entry : EntryManagement.entryListTemp) {
					entryStatement.setDate(1, java.sql.Date.valueOf(entry.getStartDate()));
					entryStatement.setDate(2, java.sql.Date.valueOf(entry.getEndDate()));
					entryStatement.setInt(3, entry.getQuantity());
					entryStatement.setInt(4, shipmentId);
					entryStatement.setString(5, entry.getProductId());
					entryStatement.executeUpdate();

					quantityStatement.setInt(1, entry.getQuantity());
					quantityStatement.setString(2, entry.getProductId());
					if (quantityStatement.executeUpdate() != 1) {
						throw new SQLException("A shipment product no longer exists.");
					}
				}

				conn.commit();
				return shipmentId;
			} catch (SQLException exception) {
				conn.rollback();
				throw exception;
			} finally {
				conn.setAutoCommit(true);
			}
		}
	}

	private void actions() {
		addProductButton.setOnAction(x -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("EntryManagement.fxml"));
				BorderPane root2 = loader.load();
				Scene scene = new Scene(root2);
				scene.getStylesheets().add(Objects.requireNonNull(
						getClass().getResource("/AshaMangmentSystem/style.css")).toExternalForm());
				Stage stage = new Stage();
				stage.setTitle("Add Product to Shipment");
				stage.setScene(scene);
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
			} catch (IOException ex) {
				MyAlert.uiError(ex);
			}
		});
		confirmButton.setOnAction(x -> {
			if (EntryManagement.entryListTemp.isEmpty()) {
				MyAlert.alert("Error", "Please add Product Before Confirmation", AlertType.ERROR);
				return;
			}
			Manufacturer selected = manufacturerComboBox.getValue();
			if (selected == null) {
				MyAlert.alert("Error", "Please select a Manufacturer", AlertType.ERROR);
				return;
			}
			if (shipmentDatePicker.getValue() == null) {
				MyAlert.alert("Error", "Date cannot be empty", AlertType.ERROR);
				return;
			}
			LocalDate shipmentDate = shipmentDatePicker.getValue();
			if (shipmentDate.isAfter(LocalDate.now())) {
				MyAlert.alert("Error", "Invalid date, must be today or before", AlertType.ERROR);
				return;
			}

			try {
				int shipmentId = addShipment(selected, shipmentDate);
				EntryManagement.entryListTemp.clear();
				manufacturerComboBox.setValue(null);
				shipmentDatePicker.setValue(null);
				MyAlert.alert("Success", "Shipment " + shipmentId + " confirmed successfully", AlertType.INFORMATION);
			} catch (SQLException e) {
				MyAlert.databaseError(e);
			}
		});
		removeButton.setOnAction(x -> {
			Entry selected = entryTable.getSelectionModel().getSelectedItem();

			if (selected == null) {
				MyAlert.alert("Error", "Please select a Product to delete", AlertType.ERROR);
				return;
			}
			EntryManagement.entryListTemp.remove(selected);

		});
	}

}
