package edmtranslate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日本語→英語翻訳用の辞書クラス。
 *
 * @author yugolf
 *
 */
public class Dictionary {
	/** 日英辞書格納マップ */
	private static final Map<String, String> dictionaryMap = getDictionay();

	/**
	 * 辞書を取得します。
	 *
	 * @return 辞書マップ
	 */
	private static Map<String, String> getDictionay() {

		Map<String, String> map = new LinkedHashMap<String, String>();

		// コネクションの確立。
		Connection connection = DBUtil.connection;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			// ステートメント生成。
			stmt = connection.createStatement();

			// クエリを実行しデータベースから辞書を取得。
			rset = stmt
					.executeQuery("select COLUMN_JA, COLUMN_EN from DICTIONARY ORDER BY LENGTH(COLUMN_JA) DESC, COLUMN_JA");

			// データベースから取得した辞書をマップに格納。
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

	/**
	 * 日本語文字列を英語に翻訳します。
	 *
	 * @param target
	 *            翻訳対象文字列（日本語）
	 * @return 翻訳結果（英語）
	 */
	public static String translate(final String target) {
		String en = target;
		for (String key : dictionaryMap.keySet()) {
			final String value = dictionaryMap.get(key);
			final int pos = en.indexOf(key);

			if (pos == 0) {
				// 先頭に置換対象が存在する場合。

				en = en.replace(key, value);
			} else if (pos >= 0) {
				// 2文字目以降に置換対象文字が存在する場合。

				if (value == null || value.length() == 0) {
					// 置換文字がnull/空文字の場合は置換対象文字を取り除く。
					en = en.replace(key, "");
				} else if ("_".equals(en.substring(pos - 1, pos))) {
					// 空文字へ置換する場合、１文字前が"_"の場合、"_"を付加しない。
					en = en.replace(key, value);
				} else {
					// 先頭に「_」を付与して置換する。
					en = en.replace(key, "_" + value);
				}
			}
		}

		// 変換結果の確認。
		if (en.matches("[A-Z0-9[_]]*")) {
			System.out.println("    [Success] " + target + "=>" + en);
		} else {
			System.out.println("    [Failure] " + target + "=>" + en);
		}
		return en;
	}
}
