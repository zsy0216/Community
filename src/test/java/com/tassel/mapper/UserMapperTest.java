package com.tassel.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 10:28
 * @Description:
 */
@SpringBootTest
public class UserMapperTest {

    @Resource
    UserMapper userMapper;

    @Test
    public void queryUserById(){
        System.out.println(userMapper.queryUserById(149));
    }
}
