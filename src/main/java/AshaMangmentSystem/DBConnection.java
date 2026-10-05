package AshaMangmentSystem;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
	private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

	private DBConnection() {
	}

	public static Connection connect() throws SQLException {
		String url = requiredSetting("ASHA_DB_URL");
		String user = requiredSetting("ASHA_DB_USER");
		String password = requiredSetting("ASHA_DB_PASSWORD");
		return DriverManager.getConnection(url, user, password);
	}

	private static String requiredSetting(String name) throws SQLException {
		String value = DOTENV.get(name);
		if (value == null || value.isBlank()) {
			throw new SQLException("Missing database setting " + name
					+ ". Copy .env.example to .env and enter your local MySQL credentials.");
		}
		return value;
	}
}
