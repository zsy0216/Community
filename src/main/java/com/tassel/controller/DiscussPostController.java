package com.tassel.controller;

import com.tassel.entity.Comment;
import com.tassel.entity.DiscussPost;
import com.tassel.entity.User;
import com.tassel.service.CommentService;
import com.tassel.service.DiscussPostService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.CommunityUtil;
import com.tassel.util.HostHolder;
import com.tassel.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/07
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

	@Resource
	DiscussPostService discussPostService;

	@Resource
	HostHolder hostHolder;

	@Resource
	UserService userService;

	@Resource
	CommentService commentService;

	@PostMapping("/insert")
	@ResponseBody
	public String insertDiscussPost(String title, String content) {
		User user = hostHolder.getUser();
		if (user == null) {
			return CommunityUtil.getJSONString(403, "你还没有登录!");
		}

		DiscussPost post = new DiscussPost();
		post.setUserId(user.getId());
		post.setTitle(title);
		post.setContent(content);
		post.setCreateTime(new Date());
		post.setStatus(0);
		post.setType(0);
		post.setCommentCount(0);
		post.setScore(0.0);
		discussPostService.insertDiscussPost(post);

		// 报错异常情况，后续统一处理
		return CommunityUtil.getJSONString(0, "帖子发布成功!");
	}

	@GetMapping("/detail/{discussPostId}")
	public String selectDiscussPostById(@PathVariable("discussPostId") Integer discussPostId, Model model, Page page) {
		// 帖子
		DiscussPost post = discussPostService.selectDiscussPostById(discussPostId);
		model.addAttribute("post", post);
		// 作者
		User user = userService.queryUserById(post.getUserId());
		model.addAttribute("user", user);

		// 评论分页
		page.setLimit(5);
		page.setPath("/discuss/detail/" + discussPostId);
		page.setRows(post.getCommentCount());

		// 评论: 给帖子的评论
		// 回复: 给评论的评论

		// 评论列表
		List<Comment> commentList = commentService.selectCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
		// 评论 VO 列表
		List<Map<String, Object>> commentVoList = new ArrayList<>();
		if (commentList != null) {
			for (Comment comment : commentList) {
				// 评论 VO
				Map<String, Object> commentVo = new HashMap<>();
				// 评论
				commentVo.put("comment", comment);
				// 作者
				commentVo.put("user", userService.queryUserById(comment.getUserId()));

				// 回复列表
				List<Comment> replyList = commentService.selectCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
				// 回复 VO 列表
				List<Map<String, Object>> replyVoList = new ArrayList<>();
				if (replyList != null) {
					for (Comment reply : replyList) {
						Map<String, Object> replyVo = new HashMap<>();
						// 回复
						replyVo.put("reply", reply);
						// 作者
						replyVo.put("user", userService.queryUserById(reply.getUserId()));
						// 回复目标
						User target = reply.getTargetId() == 0 ? null : userService.queryUserById(reply.getTargetId());
						replyVo.put("target", target);

						replyVoList.add(replyVo);
					}
				}

				// 回复
				commentVo.put("replys", replyVoList);
				// 回复数量
				int replyCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
				commentVo.put("replyCount", replyCount);
				commentVoList.add(commentVo);
			}
		}

		model.addAttribute("comments", commentVoList);

		return "site/discuss-detail";
	}
}
