package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class ShipmentDetails {
	@FXML
	private TableView<Entry> entryTable;
	@FXML
	private TableColumn<Entry, Integer> EntryIdColumn;
	@FXML
	private TableColumn<Entry, String> productIdColumn;
	@FXML
	private TableColumn<Entry, String> productNameColumn;
	@FXML
	private TableColumn<Entry, String> shipmentIdColumn;
	@FXML
	private TableColumn<Entry, Integer> amountColumn;
	@FXML
	private TableColumn<Entry, String> startDateColumn;
	@FXML
	private TableColumn<Entry, String> expiryDateColumn;
	@FXML
	private Button backButton;

	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ShipmentDetails.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();

			EntryIdColumn.setCellValueFactory(new PropertyValueFactory<>("entryId"));
			productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
			productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
			shipmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));
			amountColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
			expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
			entryTable.setItems(Catalog.getEntryList1());
			entryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

			actions();
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	public void actions() {
		backButton.setOnAction(e -> {
			Main.setMain(new ShipmentManagement().main());
		});
	}
}
