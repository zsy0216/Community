package com.tassel.service;

import com.tassel.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
public interface CommentService {

	/**
	 * 根据评论对象实体分页查询评论列表
	 *
	 * @param entityType 评论对象，评论或者帖子 对帖子评论或对评论进行评论
	 * @param entityId   评论对象 id
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

	/**
	 * 查询评论条目数
	 *
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	Integer selectCountByEntity(int entityType, int entityId);

	/**
	 * 新增评论
	 *
	 * @param comment
	 * @return
	 */
	Integer insertComment(Comment comment);

	/**
	 * 查询帖子根据 id
	 *
	 * @param id
	 * @return
	 */
	Comment selectCommentById(int id);
}
