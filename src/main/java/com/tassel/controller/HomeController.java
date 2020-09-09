package com.tassel.controller;

import com.tassel.entity.DiscussPost;
import com.tassel.entity.User;
import com.tassel.service.DiscussPostService;
import com.tassel.service.UserService;
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
 * @author Ep流苏
 * @Date: 2020/6/14 10:46
 * @Description:
 */
@Controller
public class HomeController {
    @Resource
    DiscussPostService discussPostService;
    @Resource
    UserService userService;

    @GetMapping(value = {"/", "/index"})
    public String toIndexPage(Model model, Page page) {
        // 方法调用前，SpringMVC 会自动实例化 Model 和 Page(实体对象)，并将 Page 注入 Model，
        // 所以，在 thymeleaf 中可以直接访问到 page 对象中的数据
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        Map<String, Object> map;
        if (list != null) {
            for (DiscussPost post : list) {
                map = new HashMap<>();
                map.put("post", post);
                User user = userService.queryUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }
}
