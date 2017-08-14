package ogl4jo3.shaowei.ogl4jo3.utility.string;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 字串相關工具
 * Created by ogl4jo3 on 2017/7/27.
 */

public class StringUtil {

	/**
	 * 判斷字串是否為空或null
	 *
	 * @param str String
	 * @return true false
	 */
	public static boolean isNullorEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 將金額的數字前面增加$符號以及每三位數加一個逗號
	 *
	 * @param money int
	 * @return $+money
	 */
	public static String toMoneyStr(int money) {
		return "$" + NumberFormat.getNumberInstance(Locale.getDefault()).format(money);
	}
}
