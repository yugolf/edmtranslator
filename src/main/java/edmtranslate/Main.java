package edmtranslate;

/**
 * EDMファイルの日本語→英語変換ツールのメインクラス。
 * edmtranslatorは本クラスから起動します。
 *
 * @author yugolf
 *
 */
public class Main {

	/** 入力ファイル名デフォルト値 */
	private static final String INPUT_FILE = "src/test/resources/data/input.edm";

	/** 出力ファイル名デフォルト値 */
	private static final String OUTPUT_FILE = "src/test/resources/data/output.edm";

	/**
	 * メインメソッド
	 *
	 * @param args 第一要素は入力ファイル名、第二要素は出力ファイル名。（省略可）
	 */
	public static void main(final String args[]) {

		System.out.println("start");

		// 引数から入出力EDMファイル名を取得する。
		String[] fileNames = new String[2];
		if (args.length <= 0) {
			fileNames[0] = INPUT_FILE;
			fileNames[1] = OUTPUT_FILE;
		} else if (args.length == 1) {
			fileNames[0] = args[0];
			fileNames[1] = OUTPUT_FILE;
		} else {
			fileNames[0] = args[0];
			fileNames[1] = args[1];
		}

		// EDMファイルの翻訳
		EdmTranslator translator = new EdmTranslator();
		translator.translate(fileNames);

		System.out.println("end");
	}

}
