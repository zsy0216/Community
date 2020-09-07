package com.tassel.service.impl;

import com.tassel.entity.Comment;
import com.tassel.mapper.CommentMapper;
import com.tassel.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
@Service
public class CommentServiceImpl implements CommentService {
	@Resource
	CommentMapper commentMapper;

	@Override
	public List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit) {
		return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
	}

	@Override
	public Integer selectCountByEntity(int entityType, int entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}
}
