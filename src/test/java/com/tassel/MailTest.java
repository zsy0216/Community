package com.tassel;

import com.tassel.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 18:09
 * @Description:
 */
@SpringBootTest
public class MailTest {

    @Resource
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("594983498@qq.com", "TEST", "Welcome.");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sunday");

        String content = templateEngine.process("/mail/activation", context);
        System.out.println(content);

        mailClient.sendMail("594983498@qq.com", "HTML", content);
    }

}
