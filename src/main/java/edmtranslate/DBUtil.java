package edmtranslate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * データベース接続ユーティリティクラス。
 *
 * @author yugolf
 *
 */
public class DBUtil {

	/** コネクション */
	public static final Connection connection = getConnection();

	/** データベースへのコネクションを取得します。 */
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
