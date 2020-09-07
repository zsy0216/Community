package com.tassel.service.impl;

import com.tassel.entity.DiscussPost;
import com.tassel.mapper.DiscussPostMapper;
import com.tassel.service.DiscussPostService;
import com.tassel.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:36
 * @Description:
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
	@Resource
	DiscussPostMapper discussPostMapper;

	@Resource
    SensitiveFilter sensitiveFilter;

	@Override
	public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit) {
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}

	@Override
	public Integer selectDiscussPostRows(int userId) {
		return discussPostMapper.selectDiscussPostRows(userId);
	}

	@Override
	public Integer insertDiscussPost(DiscussPost post) {
		if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
		}

		// 转义 HTML 标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
		post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

		return discussPostMapper.insertDiscussPost(post);
	}

	@Override
	public DiscussPost selectDiscussPostById(int id) {
		return discussPostMapper.selectDiscussPostById(id);
	}
}
