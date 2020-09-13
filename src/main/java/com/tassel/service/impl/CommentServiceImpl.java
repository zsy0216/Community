package com.tassel.service.impl;

import com.tassel.entity.Comment;
import com.tassel.mapper.CommentMapper;
import com.tassel.service.CommentService;
import com.tassel.service.DiscussPostService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {
	@Resource
	CommentMapper commentMapper;

	@Resource
	SensitiveFilter sensitiveFilter;

	@Resource
	DiscussPostService discussPostService;

	@Override
	public List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit) {
		return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
	}

	@Override
	public Integer selectCountByEntity(int entityType, int entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Integer insertComment(Comment comment) {
		if (comment == null) {
			throw new IllegalArgumentException("参数不能为空!");
		}
		// 过滤 HTML 标记和敏感词
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(sensitiveFilter.filter(comment.getContent()));
		// 添加评论
		int rows = commentMapper.insertComment(comment);

		// 更新帖子的评论数量
		if (comment.getEntityType() == ENTITY_TYPE_POST) {
			Integer count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
			discussPostService.updateCommentCount(comment.getEntityId(), count);
		}
		return rows;
	}

	@Override
	public Comment selectCommentById(int id) {
		return commentMapper.selectCommentById(id);
	}
}
