package com.tassel.mapper;

import com.tassel.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @Description
 * @Author Zhang Shuaiyin
 * @Date 2020/07/05
 */
@SpringBootTest
public class LoginTicketMapperTest {

    @Autowired
    LoginTicketMapper ticketMapper;

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        ticketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket abc = ticketMapper.selectByTicket("abc");
        System.out.println(abc);

        ticketMapper.updateStatus("abc", 1);
        abc = ticketMapper.selectByTicket("abc");
        System.out.println(abc);
    }
}
