package com.tassel.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description 敏感词过滤器
 * @date 2020/07/18
 */
@Component
public class SensitiveFilter {
	private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

	/**
	 * 敏感词替换符
	 */
	private static final String REPLACEMENT = "***";

	/**
	 * 根节点
	 */
	private TrieNode rootNode = new TrieNode();

	/**
	 * 初始化前缀树
	 *
	 * @PostConstruct：标注一个初始化方法，在容器初始化 bean 时，该初始化方法自动执行
	 */
	@PostConstruct
	public void init() {
		// 按行读取保存敏感词数据的文件
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String keyword;
			while ((keyword = reader.readLine()) != null) {
				// 添加到前缀树
				this.addKeyword(keyword);
			}
		} catch (IOException e) {
			logger.error("加载敏感词文件失败: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 将一个敏感词添加到前缀树中去
	 */
	private void addKeyword(String keyword) {
		TrieNode tempNode = rootNode;
		for (int i = 0; i < keyword.length(); i++) {
			char c = keyword.charAt(i);
			TrieNode subNode = tempNode.getSubNode(c);

			if (subNode == null) {
				// 初始化子节点
				subNode = new TrieNode();
				tempNode.setSubNode(c, subNode);
			}

			// 指向子节点，进入下一轮循环
			tempNode = subNode;

			// 设置结束标识
			if (i == keyword.length() - 1) {
				tempNode.isKeywordEnd = true;
			}
		}
	}

	/**
	 * 过滤敏感词算法
	 *
	 * @param text 待过滤文本
	 * @return 过滤后的文本
	 */
	public String filter(String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}

		// 指针1 默认指向前缀树根节点
		TrieNode tempNode = rootNode;
		// 指针2 默认指向文本字符串首位 只向后移动，到敏感词开始时停下
		int begin = 0;
		// 指针3 默认指向文本字符串首位，跟着指针2, 当指针2遇到敏感词停下时，指针3继续移动判断是否是敏感词
		int position = 0;
		// 结果文本
		StringBuilder sb = new StringBuilder();

		while (position < text.length()) {
			char c = text.charAt(position);
			// 跳过符号
			if (isSymbol(c)) {
				// 若指针1 处于根节点,将此符号计入结果不跳过,让指针2向下走一步
				if (tempNode == rootNode) {
					sb.append(c);
					begin++;
				}
				// 无论符号在开头或中间，指针3都向下走一步
				position++;
				continue;
			}
			// 检查下级节点
			tempNode = tempNode.getSubNode(c);
			if (tempNode == null) {
				// 以begin为开头的字符串不是敏感词
				sb.append(text.charAt(begin));
				// 进入下一个位置
				position = ++begin;
				// 重新指向根节点
				tempNode = rootNode;
			} else if (tempNode.isKeywordEnd) {
				// 发现了敏感词，将begin到position字符串替换掉
				sb.append(REPLACEMENT);
				// 进入下一个位置
				begin = ++position;
				// 重新指向根节点
				tempNode = rootNode;
			} else {
				// 继续检查下一个字符
				position++;
			}
		}
		// 将最后一批字符计入结果
		sb.append(text.substring(begin));
		return sb.toString();
	}

	/**
	 * 判断字符是否是符号
	 * 0x2E80 ~ 0x9FFF 东亚文字范围
	 *
	 * @param c
	 * @return
	 */
	private boolean isSymbol(Character c) {
		return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
	}

	/**
	 * 定义前缀树 数据结构
	 */
	private class TrieNode {
		// 关键词结束标识
		private boolean isKeywordEnd = false;

		// 子节点(key 是下级字符，value 是下级节点)
		private Map<Character, TrieNode> subNodes = new HashMap<>();

		// 添加子节点
		public void setSubNode(Character c, TrieNode node) {
			subNodes.put(c, node);
		}

		// 获取子节点
		public TrieNode getSubNode(Character c) {
			return subNodes.get(c);
		}

		public boolean isKeywordEnd() {
			return isKeywordEnd;
		}

		public void setKeywordEnd(boolean keywordEnd) {
			isKeywordEnd = keywordEnd;
		}
	}


}
