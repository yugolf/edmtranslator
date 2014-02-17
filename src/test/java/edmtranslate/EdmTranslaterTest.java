package edmtranslate;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EdmTranslaterTest {

	private final String INPUT_FILE = "src/test/resources/data/input.edm";
	private final String OUTPUT_FILE = "src/test/resources/data/output.edm";
	private final String EXPECTED_FILE = "src/test/resources/data/expected.edm";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void EDMファイルを全比較() throws Exception {
		EdmTranslater parser = new EdmTranslater();
		String[] files = { INPUT_FILE, OUTPUT_FILE };
		parser.translate(files);

		final SAXReader reader = new SAXReader();
		Document actual = reader.read(OUTPUT_FILE);
		Document expected = reader.read(EXPECTED_FILE);

		assertThat(actual.asXML(), is(expected.asXML()));
	}
}
