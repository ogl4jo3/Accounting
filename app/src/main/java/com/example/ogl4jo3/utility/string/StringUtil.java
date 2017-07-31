package com.example.ogl4jo3.utility.string;

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
}
