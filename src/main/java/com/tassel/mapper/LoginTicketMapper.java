package com.tassel.mapper;

import com.tassel.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Description
 * @Author Zhang Shuaiyin
 * @Date 2020/07/05
 */
@Mapper
public interface LoginTicketMapper {

    /**
     * 添加登录凭证
     *
     * @param loginTicket
     * @return
     */
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 通过登录凭证获取登录信息
     *
     * @param ticket
     * @return
     */
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新状态
     *
     * @param ticket
     * @param status
     * @return
     */
    @Update({ //演示动态SQL使用方法，if无实际意义
            "<script>",
            "update login_ticket set status = #{status} ",
            "where ticket = #{ticket} ",
            "<if test=\"ticket != null\">",
            "and 1=1",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);
}
