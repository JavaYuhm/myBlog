package com.myblog.controller;

import com.myblog.domain.Article;
import com.myblog.dto.response.ArticleListViewResponse;
import com.myblog.dto.response.ArticleViewResponse;
import com.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login(){
        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }

}
