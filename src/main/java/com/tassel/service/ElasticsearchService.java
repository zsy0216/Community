package com.tassel.service;

import com.tassel.entity.DiscussPost;
import org.springframework.data.domain.Page;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/16
 */
public interface ElasticsearchService {

	/**
	 * 向 Elasticsearch 服务器增加帖子信息
	 *
	 * @param post
	 */
	void saveDiscussPost(DiscussPost post);

	/**
	 * 通过 id 从 Elasticsearch 服务器删除记录
	 *
	 * @param id
	 */
	void deleteDiscussPost(int id);

	/**
	 * 查询
	 *
	 * @param keyword
	 * @param current
	 * @param limit
	 * @return
	 */
	Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit);
}
