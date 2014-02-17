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

public class EdmTranslater {

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
			input = reader.read(INPUT_FILE);
			readElements(input.getRootElement().elementIterator());

			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(
					OUTPUT_FILE), "UTF-8"),getOutputFormat());
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

	private OutputFormat getOutputFormat() {
		final OutputFormat outputFormat = new OutputFormat();
		outputFormat.setSuppressDeclaration(true);
		return outputFormat;
	}

	private void readElements(final Iterator<?> iterator) {
		while (iterator.hasNext()) {
			final Element element = (Element) iterator.next();

			// System.out.println("[" + in.getParent().getName() + "]"
			// + in.getName() + ":" + out.getName());

			translateAttributes(element.attributeIterator());

			if (element.elementIterator().hasNext()) {
				readElements(element.elementIterator());
			}
		}
	}

	private void translateAttributes(final Iterator<?> iterator) {
		while (iterator.hasNext()) {
			final DefaultAttribute attribute = (DefaultAttribute) iterator
					.next();

			final String name = attribute.getName();
			final String data = attribute.getData().toString();
			final String parent = attribute.getParent().getName();
			// System.out.println("    " + name + "=" + data);

			if (("ENTITY".equals(parent) || "ATTR".equals(parent)
					|| "INDEX".equals(parent) || "RELATION".equals(parent))
					&& ("P-NAME".equals(name))) {
				attribute.setData(Dictionary.translate(data));
			}
		}
	}
}
