package com.tassel.mapper;

import com.tassel.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:25
 * @Description:
 */
@Mapper
public interface UserMapper {

    /**
     * 根据 id 查询用户
     *
     * @param id
     * @return
     */
    User queryUserById(int id);

    /**
     * 根据名字查询用户
     *
     * @param username
     * @return
     */
    User queryByName(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email
     * @return
     */
    User queryByEmail(String email);


    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    Integer insertUser(User user);

    Integer updateStatus(Integer status, Integer id);

    Integer updateHeader(String headerUrl, Integer id);

    Integer updatePassword(String password, Integer id);
}
