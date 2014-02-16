package edmtranslate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Iterator;
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

	public static String translate(String ja) {
		final String en = replaceja(ja, dictionaryMap.keySet().iterator());
		// 変換結果の確認
		if (en.matches("[A-Z0-9[_]]*")) {
			System.out.println("    [Success] " + ja + "=>" + en);
		} else {
			System.out.println("    [Failure] " + ja + "=>" + en);
		}
		return en;
	}

	private static String replaceja(final String ja, Iterator<String> iterator) {
		if (iterator.hasNext()) {
			final String en;
			final String key = iterator.next();
			final String value = dictionaryMap.get(key);
			if (key == null || value == null) {
				return replaceja(ja, iterator);
			}
			final int pos = ja.indexOf(key);
			if (pos > 0) {
				if (value.length() == 0 || "_".equals(ja.substring(pos - 1, pos))) {
					// 空文字へ置換する場合、１文字前が"_"の場合、"_"を付加しない。
					en = ja.replace(key, value);
				} else {
					en = ja.replace(key, "_" + value);
				}
			} else {
				en = ja.replace(key, value);
			}
			return replaceja(en, iterator);
		} else {
			return ja;
		}
	}
}
