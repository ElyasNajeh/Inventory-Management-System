package AshaMangmentSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Catalog {
	private static final ObservableList<Category> categoryList = FXCollections.observableArrayList();
	private static final ObservableList<Manufacturer> manufacturerList = FXCollections.observableArrayList();
	private static final ObservableList<Product> productList = FXCollections.observableArrayList();
	private static final ObservableList<Entry> entryList = FXCollections.observableArrayList();
	private static final ObservableList<Shipment> shipmentList = FXCollections.observableArrayList();

	public static ObservableList<Category> getCategoryList() {
		categoryList.clear();
		try (Connection conn = DBConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT category_id,category_name, category_description FROM category")) {

			while (rs.next()) {
				String id = rs.getString("category_id");
				String name = rs.getString("category_name");
				String description = rs.getString("category_description");
				Category cat = new Category(id, name, description);
				categoryList.add(cat);
			}

		} catch (SQLException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
		}
		return categoryList;
	}

	public static ObservableList<Manufacturer> getManufacturerList() {
		manufacturerList.clear();
		try (Connection conn = DBConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT manufacturer_id,manufacturer_name, manufacturer_location, manufacturer_email FROM manufacturer")) {

			while (rs.next()) {
				String id = rs.getString("manufacturer_id");
				String name = rs.getString("manufacturer_name");
				String location = rs.getString("manufacturer_location");
				String email = rs.getString("manufacturer_email");
				Manufacturer manu = new Manufacturer(id, name, location, email);
				manufacturerList.add(manu);
			}

		} catch (SQLException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
		}
		return manufacturerList;
	}

	public static ObservableList<Product> getProductList() {
		productList.clear();

		try (Connection conn = DBConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT p.product_id, p.product_name, p.unit_price, p.product_description, "
								+ "p.total_quantity, p.discount, m.manufacturer_id, m.manufacturer_name, "
								+ "m.manufacturer_location, m.manufacturer_email, c.category_id, "
								+ "c.category_name, c.category_description FROM product p "
								+ "JOIN manufacturer m ON m.manufacturer_id = p.manufacturer_id "
								+ "JOIN category c ON c.category_id = p.category_id")) {

			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("product_name");
				double unitPrice = rs.getDouble("unit_price");
				String descripProduct = rs.getString("product_description");
				Manufacturer man = new Manufacturer(rs.getString("manufacturer_id"),
						rs.getString("manufacturer_name"), rs.getString("manufacturer_location"),
						rs.getString("manufacturer_email"));
				Category cat = new Category(rs.getString("category_id"), rs.getString("category_name"),
						rs.getString("category_description"));

				int totalQuantity = rs.getInt("total_quantity");
				double discount1 = rs.getDouble("discount");

				Product product = new Product(id, name, descripProduct, unitPrice, totalQuantity, discount1, man, cat);
				productList.add(product);
			}

		} catch (SQLException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
		}

		return productList;
	}

	public static ObservableList<Entry> getEntryList() {
		return entryList;
	}

	public static ObservableList<Entry> getEntryList1() {
		entryList.clear();
		Shipment s = ShipmentManagement.selected1;
		if (s == null) {
			return entryList;
		}
		String sql = "SELECT e.entry_id, e.start_date, e.expiry_date, e.quantity, e.shipment_id, "
				+ "e.product_id, p.product_name FROM entry e "
				+ "JOIN product p ON p.product_id = e.product_id WHERE e.shipment_id = ?";

		try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, s.getShipmentID());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("entry_id");
				String startDate = rs.getString("start_date");
				String expiryDate = rs.getString("expiry_date");
				int quantity = rs.getInt("quantity");
				String shipId = rs.getString("shipment_id");
				String productId = rs.getString("product_id");
				String productName = rs.getString("product_name");
				Entry entry = new Entry(id, startDate, expiryDate, quantity, shipId, productId, productName);
				entryList.add(entry);

			}

		} catch (SQLException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
		}
		return entryList;
	}

	public static ObservableList<Shipment> getShipmentList() {
		shipmentList.clear();
		try (Connection conn = DBConnection.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT s.shipment_id, s.shipment_date, m.manufacturer_id, "
						+ "m.manufacturer_name, m.manufacturer_location, m.manufacturer_email FROM shipment s "
						+ "JOIN manufacturer m ON m.manufacturer_id = s.manufacturer_id")) {

			while (rs.next()) {
				String id = rs.getString("shipment_id");
				Manufacturer man = new Manufacturer(rs.getString("manufacturer_id"),
						rs.getString("manufacturer_name"), rs.getString("manufacturer_location"),
						rs.getString("manufacturer_email"));
				String date = rs.getString("shipment_date");
				Shipment ship = new Shipment(id, man, date);
				shipmentList.add(ship);

			}

		} catch (SQLException e) {
			MyAlert.alert("Error", e.getMessage(), AlertType.ERROR);
		}
		return shipmentList;
	}

}
