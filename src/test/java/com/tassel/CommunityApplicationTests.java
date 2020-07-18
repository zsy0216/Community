package com.tassel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    // 不能使用三目运算符
    @Value("${server.servlet.context-path = '/' ? '' : ${server.servlet.context-path}}")
    private String contextPath;

    @Test
    void contextLoads() {

        String[] imageSuffix = new String[] {"png", "jpg", "bmp", "gif", "svg", "webp"};
        List<String> list = Arrays.asList(imageSuffix);
        String suffix = "jpg";
        System.out.println(list.contains(suffix));
    }

}
