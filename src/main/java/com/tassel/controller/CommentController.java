package com.tassel.controller;

import com.tassel.entity.Comment;
import com.tassel.entity.DiscussPost;
import com.tassel.entity.Event;
import com.tassel.event.EventProducer;
import com.tassel.service.CommentService;
import com.tassel.service.DiscussPostService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/08
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
	@Resource
	CommentService commentService;

	@Resource
	HostHolder hostHolder;

	@Resource
	EventProducer eventProducer;

	@Resource
	DiscussPostService discussPostService;

	@PostMapping("/add/{discussPostId}")
	public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		if (comment.getTargetId() == null) {
			comment.setTargetId(0);
		}
		commentService.insertComment(comment);

		// 触发评论事件
		Event event = new Event().setTopic(TOPIC_COMMENT).setUserId(hostHolder.getUser().getId()).setEntityType(comment.getEntityType()).setEntityId(comment.getEntityId()).setData("postId", discussPostId);
		if (comment.getEntityType() == ENTITY_TYPE_POST) {
			DiscussPost target = discussPostService.selectDiscussPostById(comment.getEntityId());
			event.setEntityUserId(target.getUserId());
		} else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
			Comment target = commentService.selectCommentById(comment.getEntityId());
			event.setEntityUserId(target.getUserId());
		}
		eventProducer.fireEvent(event);

		return "redirect:/discuss/detail/" + discussPostId;
	}
}
