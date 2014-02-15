package edmtranslate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;

public class EdmTranslater {

	public void translate(String[] fileNames) {
		
		if(fileNames.length != 2){
			throw new IllegalAccessError();
		}
		
		final String INPUT_FILE = fileNames[0];
		final String OUTPUT_FILE = fileNames[1];

		final SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		XMLWriter writer = null;
		Document input;
		final Document output = DocumentHelper.createDocument();
		final OutputFormat outputFormat = getOutputFormat();

		try {
			input = reader.read(INPUT_FILE);
			final Element inRoot = input.getRootElement();
			final Element outRoot = output.addElement(inRoot.getName());

			setAttributes(inRoot.attributeIterator(), outRoot);
			setElements(inRoot.elementIterator(), outRoot);

			writer = new XMLWriter(new FileWriter(OUTPUT_FILE), outputFormat);
			writer.write(output);

		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(writer != null){
					writer.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private OutputFormat getOutputFormat() {
		final OutputFormat outputFormat = new OutputFormat("  ", true, "UTF-8");
		outputFormat.setSuppressDeclaration(true);
		outputFormat.setNewLineAfterDeclaration(false);
		return outputFormat;
	}

	private void setElements(final Iterator<?> iterator, final Element preout) {
		if (iterator.hasNext()) {
			final Element in = (Element)iterator.next();
			final Element out = preout.addElement(in.getName());

//			System.out.println("[" + in.getParent().getName() + "]"
//					+ in.getName() + ":" + out.getName());

			setAttributes(in.attributeIterator(), out);

			if (in.elementIterator().hasNext()) {
				setElements(in.elementIterator(), out);
			}

			setElements(iterator, preout);
		}
	}

	private void setAttributes(final Iterator<?> iterator,
			final Element element) {
		if (iterator.hasNext()) {
			final DefaultAttribute attribute = (DefaultAttribute)iterator.next();

			final String name = attribute.getName();
			final String data = attribute.getData().toString();
			final String parent = attribute.getParent().getName();
			//System.out.println("    " + name + "=" + data);

			if (("ENTITY".equals(parent) || "ATTR".equals(parent) || "INDEX"
					.equals(parent)) && ("P-NAME".equals(name))) {
				String en = Dictionary.translate(data);
				element.addAttribute(name, en);
			} else {
				element.addAttribute(name, data);
			}
			setAttributes(iterator, element);
		}
	}
}
