package edmtranslate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dictionary {
	private static final Map<String, String> dictionaryMap = getDictionay();

	private static Map<String, String> getDictionay() {

		Map<String, String> map = new LinkedHashMap<String, String>();

		Connection connection = DBUtil.connection;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = connection.createStatement();

			rset = stmt
					.executeQuery("select COLUMN_JA, COLUMN_EN from DICTIONARY ORDER BY LENGTH(COLUMN_JA) DESC, COLUMN_JA");

			while (rset.next()) {
				map.put(rset.getString("COLUMN_JA"),
						StringUtil.decamelize(rset.getString("COLUMN_EN")));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	public static String translate(String target) {
		final String orgstr = target;
		for (String key : dictionaryMap.keySet()) {
			final String value = dictionaryMap.get(key);
			final int pos = target.indexOf(key);
			if (pos >= 0) {
				if (value == null || value.length() == 0) {
					target = target.replace(key, "");
				} else if ("_".equals(target.substring(pos - 1, pos))) {
					// 空文字へ置換する場合、１文字前が"_"の場合、"_"を付加しない。
					target = target.replace(key, value);
				} else {
					target = target.replace(key, "_" + value);
				}
			} else if (pos == 0) {
				target = target.replace(key, value);
			} else {
				target = target.replace(key, "");
			}
		}

		// 変換結果の確認
		if (target.matches("[A-Z0-9[_]]*")) {
			System.out.println("    [Success] " + orgstr + "=>" + target);
		} else {
			System.out.println("    [Failure] " + orgstr + "=>" + target);
		}
		return target;
	}
}
