package com.tassel.controller;

import com.tassel.entity.DiscussPost;
import com.tassel.service.ElasticsearchService;
import com.tassel.service.LikeService;
import com.tassel.service.UserService;
import com.tassel.util.CommunityConstant;
import com.tassel.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuaiyin.zhang
 * @description
 * @date 2020/09/16
 */
@Controller
public class SearchController implements CommunityConstant {

	@Resource
	ElasticsearchService elasticsearchService;

	@Resource
	UserService userService;

	@Resource
	LikeService likeService;

	@GetMapping("/search")
	public String search(String keyword, Page page, Model model) {
		// 搜索帖子
		org.springframework.data.domain.Page<DiscussPost> searchResult = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
		// 聚合数据
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if (searchResult != null) {
			for (DiscussPost post : searchResult) {
				Map<String, Object> map = new HashMap<>();
				// 帖子
				map.put("post", post);
				// 作者
				map.put("user", userService.queryUserById(post.getUserId()));
				// 点赞数量
				map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		model.addAttribute("keyword", keyword);

		// 分页信息
		page.setPath("/search?keyword=" + keyword);
		page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
		return "/site/search";
	}
}
