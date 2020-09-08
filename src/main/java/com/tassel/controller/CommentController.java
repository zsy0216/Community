package com.tassel.controller;

import com.tassel.entity.Comment;
import com.tassel.service.CommentService;
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
public class CommentController {
	@Resource
	CommentService commentService;

	@Resource
	HostHolder hostHolder;

	@PostMapping("/add/{discussPostId}")
	public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		if (comment.getTargetId() == null) {
			comment.setTargetId(0);
		}
		commentService.insertComment(comment);

		return "redirect:/discuss/detail/" + discussPostId;
	}
}
