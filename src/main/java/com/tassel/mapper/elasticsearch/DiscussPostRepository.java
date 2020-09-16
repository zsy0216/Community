package com.tassel.mapper.elasticsearch;

import com.tassel.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/15
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
