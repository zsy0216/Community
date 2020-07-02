package com.tassel.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author Ep流苏
 * @since 2020-06-14 09:43:19
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 708453489701305063L;
    
    private Integer id;
    
    private String username;
    
    private String password;
    
    private String salt;
    
    private String email;
    /**
    * 0-普通用户; 1-超级管理员; 2-版主;
    */
    private Integer type;
    /**
    * 0-未激活; 1-已激活;
    */
    private Integer status;
    
    private String activationCode;
    
    private String headerUrl;
    
    private Date createTime;

}