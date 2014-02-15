package edmtranslate;

public class StringUtil {
	public static String decamelize(final String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.toUpperCase();
		}
		StringBuffer buf = new StringBuffer(40);
		int pos = 0;
		for (int i = 1; i < s.length(); ++i) {
			if (Character.isUpperCase(s.charAt(i))) {
				if (buf.length() != 0) {
					buf.append('_');
				}
				buf.append(s.substring(pos, i).toUpperCase());
				pos = i;
			}
		}
		if (buf.length() != 0) {
			buf.append('_');
		}
		buf.append(s.substring(pos, s.length()).toUpperCase());
		return buf.toString();
	}
}
