package com.tassel.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 16:29
 * @Description:
 */
public class CommunityUtil {

	/**
	 * 生成随机字符串
	 *
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * MD5 加密
	 * 加盐值
	 */
	public static String md5(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}

	/**
	 * @param contextPath
	 * @return 判断contextPath 是否为 / 防止url 多个 / 导致拼接失败
	 */
	public static String contextPathJudge(String contextPath) {
		return "/".equals(contextPath) ? "" : contextPath;
	}

	/**
	 * 生成 json 字符串
	 *
	 * @param code
	 * @param msg
	 * @param map
	 * @return
	 */
	public static String getJSONString(int code, String msg, Map<String, Object> map) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		if (map != null) {
			for (String key : map.keySet()) {
				json.put(key, map.get(key));
			}
		}
		return json.toJSONString();
	}

	public static String getJSONString(int code, String msg) {
		return getJSONString(code, msg, null);
	}

	public static String getJSONString(int code) {
		return getJSONString(code, null, null);
	}
}
