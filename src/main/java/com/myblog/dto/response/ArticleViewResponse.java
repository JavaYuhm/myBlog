package com.myblog.dto.response;

import com.myblog.domain.Article;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleViewResponse {

    private  Long id;
    private  String title;
    private  String content;

    private  LocalDateTime createdAt;


    @Builder
    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
    }


    public ArticleViewResponse() {

    }
}
