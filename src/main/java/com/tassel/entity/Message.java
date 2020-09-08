package com.tassel.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Message)实体类
 *
 * @author Ep流苏
 * @since 2020-09-08 15:32:40
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = -58858664784963809L;
    
    private Integer id;
    
    private Integer fromId;
    
    private Integer toId;
    
    private String conversationId;
    
    private String content;
    /**
    * 0-未读;1-已读;2-删除;
    */
    private Integer status;
    
    private Date createTime;
}