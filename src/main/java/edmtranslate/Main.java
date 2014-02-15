package edmtranslate;

public class Main {

	private static final String INPUT_FILE = "src/test/resources/data/input.edm";
	private static final String OUTPUT_FILE = "src/test/resources/data/output.edm";

	public static void main(String args[]) {

		System.out.println("start");
		
		String[] fileNames = new String[2];
		if(args.length <= 0) {
			fileNames[0] = INPUT_FILE;
			fileNames[1] = OUTPUT_FILE;
		} else if (args.length == 1) {
			fileNames[0] = INPUT_FILE;
			fileNames[1] = args[0];
		} else {
			fileNames[0] = args[0];
			fileNames[1] = args[1];
		}
		
		EdmTranslater translater = new EdmTranslater();
		translater.translate(fileNames);

		System.out.println("end");
	}

}
