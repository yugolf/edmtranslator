package edmtranslate;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Dictionaryクラスのテストクラス。
 *
 * @author yugolf
 *
 */
public class DictionaryTest {

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
	public void プロジェクト名() {
		String actual = Dictionary.translate("プロジェクト名");
		String expected = "PROJECT_NM";
		assertThat(actual, is(expected));
	}

	@Test
	public void FK_顧客_プロジェクト() {
		String actual = Dictionary.translate("FK_顧客_プロジェクト");
		String expected = "FK_CUSTOMER_PROJECT";

		assertThat(actual, is(expected));
	}

	@Test
	public void 顧客ＩＤ() {
		String actual = Dictionary.translate("顧客ＩＤ");
		String expected = "CUSTOMER_ID";

		assertThat(actual, is(expected));
	}

}
