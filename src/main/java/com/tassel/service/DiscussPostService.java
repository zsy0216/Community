package com.tassel.service;

import com.tassel.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:33
 * @Description:
 */
public interface DiscussPostService {

    /**
     * 查询
     *
     * @param userId 可选值, 用户 id
     * @param offset 起始数据号
     * @param limit  每页数据条数
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 查询行数
     *
     * @param userId 如果参数只有一个，而且可能要动态拼接SQL，必须加 @Param 注解
     * @return
     */
    Integer selectDiscussPostRows(@Param("userId") int userId);
}
