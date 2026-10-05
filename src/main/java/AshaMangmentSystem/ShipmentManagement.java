package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ShipmentManagement {

	@FXML
	private TableView<Shipment> shipmentTable;

	@FXML
	private TableColumn<Shipment, Integer> shipmentIdColumn;

	@FXML
	private TableColumn<Shipment, String> shipmentDateColumn;

	@FXML
	private TableColumn<Shipment, String> manufacturerColumn;

	@FXML
	private Button updateButton;

	@FXML
	private Button removeButton;

	@FXML
	private Button showDetailsButton;

	static Shipment selected1;

	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ShipmentManagement.fxml"));
			BorderPane root = loader.load();

			ShipmentManagement controller = loader.getController();
			controller.initializeColumns();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	public void initializeColumns() {
		shipmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentID"));
		manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		shipmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		shipmentTable.setItems(Catalog.getShipmentList());
		actions();

	}

	public void deleteButton() {
		Shipment selected = shipmentTable.getSelectionModel().getSelectedItem();

		if (selected == null) {
			MyAlert.alert("Error", "Please select a Shipment to delete", AlertType.ERROR);
			return;
		}
		boolean confirmation = MyAlert.alert("Confirmation", "Are you sure you need to Delete this Shipment ? ",
				AlertType.CONFIRMATION);
		if (!confirmation) {
			return;
		}
		String deleteSql = "DELETE FROM shipment WHERE shipment_id = ?";
		String refreshQuantitiesSql = "UPDATE product p LEFT JOIN "
				+ "(SELECT product_id, SUM(quantity) AS quantity FROM entry GROUP BY product_id) e "
				+ "ON e.product_id = p.product_id SET p.total_quantity = COALESCE(e.quantity, 0)";
		try (Connection conn = DBConnection.connect()) {
			conn.setAutoCommit(false);
			try (PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
					PreparedStatement quantityStatement = conn.prepareStatement(refreshQuantitiesSql)) {
				deleteStatement.setString(1, selected.getShipmentID());
				deleteStatement.executeUpdate();
				quantityStatement.executeUpdate();
				conn.commit();
			} catch (SQLException exception) {
				conn.rollback();
				throw exception;
			} finally {
				conn.setAutoCommit(true);
			}
			MyAlert.alert("Success", "Shipment Deleted Successfully ", AlertType.INFORMATION);
			shipmentTable.setItems(Catalog.getShipmentList());
		} catch (SQLException e) {
			MyAlert.databaseError(e);
		}
	}

	private void actions() {
		updateButton.setOnAction(x -> {
			try {
				Shipment selected = shipmentTable.getSelectionModel().getSelectedItem();

				if (selected == null) {
					MyAlert.alert("Error", "Please select a Shipment to Update", AlertType.ERROR);
					return;
				}

				FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateShipment.fxml"));
				BorderPane root = loader.load();

				UpdateShipment controller = loader.getController();
				controller.setShipment(selected);

				Stage stage = new Stage();
				stage.setTitle("Update Shipment");
				Scene scene = new Scene(root);
				scene.getStylesheets().add(Objects.requireNonNull(
						getClass().getResource("/AshaMangmentSystem/style.css")).toExternalForm());
				stage.setScene(scene);
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				shipmentTable.refresh();
			} catch (IOException e) {
				MyAlert.uiError(e);
			}
		});
		removeButton.setOnAction(x -> {
			deleteButton();
		});
		showDetailsButton.setOnAction(x -> {
			selected1 = shipmentTable.getSelectionModel().getSelectedItem();
			if (selected1 == null) {
				MyAlert.alert("Error", "Please select a Shipment to Show Details", AlertType.ERROR);
				return;
			}
			Main.setMain(new ShipmentDetails().main());
		});
	}
}
