package com.tassel;

import com.tassel.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/07/18
 */
@SpringBootTest
public class SensitiveTests {
	@Resource
	SensitiveFilter sensitiveFilter;

	@Test
	public void testSensitiveFilter() {
		String text = "这里可以赌博，可以嫖娼，可以开票，可以吸毒，哈哈哈!";
		text = "这里可以☆赌☆博☆，可以☆嫖☆娼☆，可以☆开☆票☆，可以☆吸☆毒☆，哈哈哈!";
		String filter = sensitiveFilter.filter(text);
		System.out.println(filter);
	}
}
