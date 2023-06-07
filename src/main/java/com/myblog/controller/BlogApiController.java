package com.myblog.controller;

import com.myblog.domain.Article;
import com.myblog.dto.request.ArticleRequest;
import com.myblog.dto.response.ArticleResponse;
import com.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleRequest request){
        Article savedArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticle(){
        List<ArticleResponse> articles = blogService
                .findAll()
                .stream().map(ArticleResponse::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> upateArticle(@PathVariable long id, @RequestBody ArticleRequest request){
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(updateArticle);
    }
}
