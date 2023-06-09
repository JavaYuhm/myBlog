package com.myblog.service;

import com.myblog.domain.Article;
import com.myblog.dto.request.ArticleRequest;
import com.myblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor // final or @NotNull 이 붙은 필드의 생성자 추가
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    // 블로그 글 추가
    public Article save(ArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

    // 블로그 글 조회(전체)
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    // 블로그 글 삭제
    public void delete(long id){

        Article article = blogRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        blogRepository.deleteById(id);
        authorizeArticleAuthor(article);

    }

    // 블로그 수정
    @Transactional
    public Article update(long id, ArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not Found :"+id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    public Article findById(Long id){
        return blogRepository.findById(id).orElseThrow(()-> new RuntimeException("ID를 찾을 수 없음"));
    }

    private static void authorizeArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("Not authorized");
        }
    }
}
