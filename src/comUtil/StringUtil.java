package comUtil;

public class StringUtil {
	public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
}
