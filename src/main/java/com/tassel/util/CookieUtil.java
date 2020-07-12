package com.tassel.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author Zhang Shuaiyin
 * @Date 2020/07/12
 */
public class CookieUtil {
	public static String getValue(HttpServletRequest request, String name) {
		if (request == null || name == null || "".equals(name)) {
			throw new IllegalArgumentException("参数为空!");
		}

		Cookie[] cookies = request.getCookies();
		if (cookies.length != 0 || cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
