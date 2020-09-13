package com.tassel.mapper;

import com.tassel.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
@Mapper
public interface CommentMapper {

	/**
	 * 根据评论对象实体分页查询评论列表
	 *
	 * @param entityType 评论对象，用户或者帖子
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
	@Select("select `id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time` from community.comment where `id` = #{id}")
	Comment selectCommentById(int id);
}
