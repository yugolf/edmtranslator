package edmtranslate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	public static final Connection connection = getConnection();

	private static Connection getConnection() {
		final Connection connection;
		try {
			final String url = "jdbc:h2:tcp://localhost:9092/demo;SCHEMA=LABO";
			final String user = "sa";
			final String pass = "";
			connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return connection;
	}
}
