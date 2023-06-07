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
    public ArticleRequest(String title, String content){
        this.title = title;
        this.content = content;
    }

    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

}
