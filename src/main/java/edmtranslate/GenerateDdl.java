package edmtranslate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * EDMファイルの翻訳処理を行うクラス。
 *
 * @author yugolf
 *
 */
public class GenerateDdl {

	private static final String BR = "\r\n";
	private static final String TAB = "    ";

	public void generate(final String[] fileNames) {

		if (fileNames.length != 2) {
			throw new IllegalAccessError();
		}

		final String INPUT_FILE = fileNames[0];
		final String OUTPUT_FILE = fileNames[1];

		final SAXReader reader = new SAXReader();
		final Document input;
		File out = new File(OUTPUT_FILE);
		FileWriter writer = null;

		try {
			// INPUTファイルの読み込み。
			input = reader.read(INPUT_FILE);

			// EDMの全エレメントを取得し翻訳処理を行う。
			List<Element> entityList = input.selectNodes("/ERD/ENTITY");

			// DDLの作成
			String ddl = createDdl(entityList);

			// SQLファイルの出力
			writer = new FileWriter(out);
			writer.write(ddl);

		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private String createDdl(List<Element> elements) {
		StringBuilder buf = new StringBuilder();
		for (Element element : elements) {
			String pname = element.attributeValue("P-NAME");

			buf.append("CREATE TABLE " + pname + BR);
			buf.append("(" + BR);

			// カラムリストを出力する。
			List<Element> attrList = element.elements("ATTR");
			buf.append(createAttr(attrList) + BR);

			buf.append(")" + BR);
			buf.append("/" + BR);

			// INDEXの生成文を出力する。
			List<Element> indexList = element.elements("INDEX");
			String createindex = createIndex(indexList);

			buf.append(createindex);
			buf.append("/" + BR);
		}

		return buf.toString();
	}

	private StringBuilder createAttr(List<Element> elements) {
		StringBuilder buf = new StringBuilder();
		for (Element element : elements) {
			if (buf.length() > 0) {
				buf.append("," + BR);
			}
			String attrpname = element.attributeValue("P-NAME");
			String attrdatatype = element.attributeValue("DATATYPE");
			String attrlength = element.attributeValue("LENGTH");
			String attrscale = element.attributeValue("SCALE");
			String attrnull = element.attributeValue("NULL");
			buf.append(TAB + nameFormat(attrpname) + attrdatatype
					+ lengthFormat(attrlength) + nullFormat(attrnull));
		}

		return buf;
	}

	private String createIndex(List<Element> elements) {
		StringBuilder buf = new StringBuilder();
		for (Element element : elements) {
			String itype = element.attributeValue("I-TYPE");
			String pkName = element.attributeValue("P-NAME");
			Element parent = element.getParent();

			// キーリストを生成する。
			List<Element> columnList = element.elements("COLUMN");
			StringBuilder key = new StringBuilder();
			for (Element column : columnList) {
				if (key.length() > 0) {
					key.append(", ");
				}
				String id = column.attributeValue("ID");
				Element keyattr = (Element) parent.selectObject("ATTR[@ID='"
						+ id + "']");
				key.append(keyattr.attributeValue("P-NAME"));
			}

			String tableName = parent.attributeValue("P-NAME");

			// プライマリキーのみ対応
			if ("0".equals(itype)) {
				buf.append("ALTER TABLE " + tableName + BR);
				buf.append(TAB + "ADD(CONSTRAINT " + pkName + " PRIMARY KEY ("
						+ key + ") USING INDEX)" + BR);
			}
		}

		return buf.toString();
	}

	private String lengthFormat(String length) {
		if ("0".equals(length)) {
			return "";
		} else {
			return "(" + length + ")";
		}
	}

	private String nameFormat(String name) {
		return StringUtils.rightPad(name, 32);
	}

	private String nullFormat(String nullable) {
		if ("0".equals(nullable)) {
			return "";
		} else {
			return " NOT NULL";
		}
	}

}
