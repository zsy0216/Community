package com.tassel.entity;

import lombok.ToString;

import java.util.Date;
import java.io.Serializable;

/**
 * (LoginTicket)实体类
 *
 * @author Ep流苏
 * @since 2020-07-05 09:39:34
 */
@ToString
public class LoginTicket implements Serializable {
    private static final long serialVersionUID = 438342979500803230L;
    
    private Integer id;
    
    private Integer userId;
    
    private String ticket;
    /**
    * 0-有效; 1-无效;
    */
    private Integer status;
    
    private Date expired;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

}