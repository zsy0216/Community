package com.tassel.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Comment)实体类
 *
 * @author Ep流苏
 * @since 2020-09-07 16:24:29
 */
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = -66137005452989538L;
    
    private Integer id;
    
    private Integer userId;
    
    private Integer entityType;
    
    private Integer entityId;
    
    private Integer targetId;
    
    private String content;
    
    private Integer status;
    
    private Date createTime;
}