package com.tassel.mapper;

import com.tassel.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:11
 * @Description:
 */
@SpringBootTest
public class DiscussPostMapperTest {
    @Resource
    DiscussPostMapper discussPostMapper;

    @Test
    public void selectDiscussPostsTest() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        discussPosts.forEach(discussPost -> System.out.println(discussPost));
    }

    @Test
    public void selectDiscussPostRowsTest() {
        Integer rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
