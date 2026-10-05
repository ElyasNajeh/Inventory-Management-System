package AshaMangmentSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayStatistical {

	@FXML
	private TextArea productsShipmentsArea;

	@FXML
	public BorderPane main() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayStatistical.fxml"));
			loader.setController(this);
			BorderPane root = loader.load();

			productsShipmentsArea
					.setText("Display all available products in the system, sorted in ascending order by expiry date:\n"
							+ "-- Answer:\n" + query1() + "\n\n" +

							"Retrieve products categorized 'Drinks', sorted by price in descending order:\n"
							+ "-- Answer:\n" + query2() + "\n\n" +

							"Display a list of expired products (quantity = 0):\n" + "-- Answer:\n" + query3() + "\n\n"
							+

							"Display a list of expired products:\n" + "-- Answer:\n" + query4() + "\n\n" +

							"Products that have a discount:\n" + "-- Answer:\n" + query5() + "\n\n" +

							"Categories with the number of products in each category:\n" + "-- Answer:\n" + query6()
							+ "\n\n");
			return root;
		} catch (IOException e) {
			MyAlert.uiError(e);
			return null;
		}
	}

	public String query1() {
		String sql = "SELECT DISTINCT p.product_id, p.product_name, e.shipment_id, e.expiry_date "
				+ "FROM product p JOIN entry e ON e.product_id = p.product_id "
				+ "WHERE e.quantity > 0 ORDER BY e.expiry_date";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("product_name");
				String ship_id = rs.getString("shipment_id");
				String date = rs.getString("expiry_date");
				result.append("Product ID: ").append(id).append(" Product Name: ").append(name).append(" Shipment ID :")
						.append(ship_id).append(" Expiry Date ").append(date).append("\n");
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}

	public String query2() {
		String sql = "SELECT DISTINCT p.product_id, p.product_name, p.unit_price, c.category_name "
				+ "FROM product p JOIN category c ON c.category_id = p.category_id "
				+ "WHERE c.category_name = ? ORDER BY p.unit_price DESC";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "Drinks");
			try (ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					String id = rs.getString("product_id");
					String name = rs.getString("product_name");
					String price = rs.getString("unit_price");
					String catName = rs.getString("category_name");
					result.append("Product ID: ").append(id).append(" Product Name: ").append(name)
							.append(" Unit Price :").append(price).append(" Category Name ").append(catName).append("\n");
				}
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}

	public String query3() {
		String sql = "SELECT DISTINCT p.product_id, p.product_name, e.expiry_date "
				+ "FROM product p JOIN entry e ON e.product_id = p.product_id "
				+ "WHERE e.expiry_date < CURDATE() AND p.total_quantity = 0";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("product_name");
				String date = rs.getString("expiry_date");
				result.append("Product ID: ").append(id).append(" Product Name: ").append(name).append(" Expiry date :")
						.append(date).append("\n");
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}

	public String query4() {
		String sql = "SELECT DISTINCT p.product_id, p.product_name, e.expiry_date "
				+ "FROM product p JOIN entry e ON e.product_id = p.product_id "
				+ "WHERE e.expiry_date < CURDATE()";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("product_name");
				String date = rs.getString("expiry_date");
				result.append("Product ID: ").append(id).append(" Product Name: ").append(name).append(" Expiry date :")
						.append(date).append("\n");
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}

	public String query5() {
		String sql = "SELECT DISTINCT p.product_id, p.product_name FROM product p WHERE p.discount > 0";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String id = rs.getString("product_id");
				String name = rs.getString("product_name");
				result.append("Product ID: ").append(id).append(" Product Name: ").append(name).append("\n");
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}

	public String query6() {
		String sql = "SELECT COUNT(p.product_id) AS product_count, c.category_name FROM category c "
				+ "LEFT JOIN product p ON p.category_id = c.category_id GROUP BY c.category_id, c.category_name";

		StringBuilder result = new StringBuilder();

		try (Connection conn = DBConnection.connect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String name = rs.getString("category_name");
				int count = rs.getInt("product_count");
				result.append(" Category Name: ").append(name).append(" Number of Products: ").append(count)
						.append("\n");
			}

		} catch (SQLException e) {
			result.append("Error: ").append(e.getMessage());
		}

		return result.toString();
	}
}
