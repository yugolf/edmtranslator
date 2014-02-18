package edmtranslate;

import org.junit.Test;

public class MainTest {

	@Test
	public void 引数なし() {
		Main.main(new String[] {});
	}

	@Test
	public void 引数ひとつ() {
		Main.main(new String[] { "src/test/resources/data/input.edm" });
	}

	@Test
	public void 引数ふたつ() {
		Main.main(new String[] { "src/test/resources/data/input.edm",
				"src/test/resources/data/output.edm" });
	}

}
