package com.myblog.dto.request;

import com.myblog.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticleRequest {
    private String title;
    private String content;


    @Builder
    public ArticleRequest(String title, String content, String author){
        this.title = title;
        this.content = content;
    }

    public Article toEntity(String author){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

}
