package AshaMangmentSystem;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
	private static BorderPane main;

	public Main() {
		main = new BorderPane();
	}

	public static void setMain(Node node) {
		main.setCenter(Objects.requireNonNull(node, "The requested screen could not be loaded"));
	}

	@Override
	public void start(Stage primaryStage) {

		SideButtons mat = new SideButtons(primaryStage);
		main.setLeft(mat.main());
		main.setStyle("-fx-background-color: #C8C8C8;");
		main.getLeft().setStyle("-fx-background-color: #0d1b58; -fx-pref-width: 100;");

		setMain(new ProductManagement().main());

		Scene scene = new Scene(main, 900, 700);
		URL stylesheet = Objects.requireNonNull(getClass().getResource("/AshaMangmentSystem/style.css"),
				"Missing application stylesheet");
		scene.getStylesheets().add(stylesheet.toExternalForm());
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Asha Management System");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
