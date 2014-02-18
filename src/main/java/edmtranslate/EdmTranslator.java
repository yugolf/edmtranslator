package edmtranslate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;

/**
 * EDMファイルの翻訳処理を行うクラス。
 *
 * @author yugolf
 *
 */
public class EdmTranslator {

	/**
	 * EDMファイルを翻訳します。
	 *
	 * @param fileNames
	 *            第一要素は入力ファイル名、第二要素は出力ファイル名。配列の要素数は必ず2であること。
	 */
	public void translate(String[] fileNames) {

		if (fileNames.length != 2) {
			throw new IllegalAccessError();
		}

		final String INPUT_FILE = fileNames[0];
		final String OUTPUT_FILE = fileNames[1];

		final SAXReader reader = new SAXReader();
		XMLWriter writer = null;
		final Document input;

		try {
			// INPUTファイルの読み込み。
			input = reader.read(INPUT_FILE);

			// EDMの全エレメントを取得し翻訳処理を行う。
			readElements(input.getRootElement().elementIterator());

			// 翻訳した結果をファイルに出力。
			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(
					OUTPUT_FILE), "UTF-8"), getOutputFormat());
			writer.write(input);
			writer.flush();

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

	/**
	 * XMLWriterのインスタンス生成時に必要なフォーマット情報を取得する。
	 *
	 * @return OutputFormat XMLWriterのフォーマット
	 */
	private OutputFormat getOutputFormat() {
		final OutputFormat outputFormat = new OutputFormat();

		// XML宣言は出力しない。
		outputFormat.setSuppressDeclaration(true);
		return outputFormat;
	}

	/**
	 * EDMの全エレメントを取得し翻訳処理を行う。
	 *
	 * @param iterator
	 *            EDMのエレメントイテレータ
	 */
	private void readElements(final Iterator<?> iterator) {
		while (iterator.hasNext()) {
			final Element element = (Element) iterator.next();

			// エレメント内のアトリビュートの翻訳。
			translateAttributes(element.attributeIterator());

			// エレメントの子要素を読み込む。
			if (element.elementIterator().hasNext()) {
				readElements(element.elementIterator());
			}
		}
	}

	/**
	 * エレメント内のアトリビュートの翻訳を行う。
	 *
	 * @param iterator
	 *            アトリビュートイテレータ
	 */
	private void translateAttributes(final Iterator<?> iterator) {
		while (iterator.hasNext()) {
			final DefaultAttribute attribute = (DefaultAttribute) iterator
					.next();
			final String name = attribute.getName();
			final String data = attribute.getData().toString();
			final String parent = attribute.getParent().getName();

			// 「ENTITY」「ATTR」「INDEX」「RELATION」エレメントの「P-NAME」を翻訳。
			if (("ENTITY".equals(parent) || "ATTR".equals(parent)
					|| "INDEX".equals(parent) || "RELATION".equals(parent))
					&& ("P-NAME".equals(name))) {
				attribute.setData(Dictionary.translate(data));
			}
		}
	}
}
