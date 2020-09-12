package com.tassel.util;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 18:46
 * @Description:
 */
public interface CommunityConstant {

    /**
     * 注册邮件激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 注册邮件重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 注册邮件激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态下登录凭证失效时间: 12 小时
     */
    int DEFAULT_EXPIRED_SECONDS = 60 * 60 * 12;
    /**
     * 记住状态下登录凭证失效时间: 100 天
     */
    int REMEMBER_EXPIRED_SECONDS = 60 * 60 * 24 * 100;

    /**
     * 评论实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;
    /**
     * 评论实体类型：评论 对评论进行评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 关注实体类型：用户 关注用户
     */
    int ENTITY_TYPE_USER = 3;
}
