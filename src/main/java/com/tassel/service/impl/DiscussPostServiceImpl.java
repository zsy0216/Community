package com.tassel.service.impl;

import com.tassel.entity.DiscussPost;
import com.tassel.mapper.DiscussPostMapper;
import com.tassel.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public Integer selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
