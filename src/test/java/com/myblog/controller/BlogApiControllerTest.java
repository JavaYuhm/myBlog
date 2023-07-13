package com.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.domain.Article;
import com.myblog.domain.User;
import com.myblog.dto.request.ArticleRequest;
import com.myblog.repository.BlogRepository;
import com.myblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@AutoConfigureMockMvc
@SpringBootTest
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;
    User user;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @BeforeEach
    public void setSecurityContext(){
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                        .email("user@gmail.com")
                        .password("test")
                .build());

        SecurityContext context1 = SecurityContextHolder.getContext();
        context1.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle : 블로그 글 추가")
    @Test
    public void addArticle() throws Exception{

        final String url = "/api/articles";
        final String title = "testTitle";
        final String content = "testContent";

        final ArticleRequest articleRequest
                = ArticleRequest.builder()
                .title(title)
                .content(content)
                .build();

        final String requestBody = objectMapper.writeValueAsString(articleRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
                        .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articleList = blogRepository.findAll();

        assertThat(articleList.size()).isEqualTo(1);
        assertThat(articleList.get(0).getTitle()).isEqualTo("testTitle");
        assertThat(articleList.get(0).getContent()).isEqualTo("testContent");

    }

    @DisplayName("findAllArticle : 블로그 글 조회(전체)")
    @Test
    public void findAllArticle() throws Exception{

        final String url = "/api/articles";


        Article savedArticle = createDefalutArticle();

        blogRepository.save(savedArticle);

        final ResultActions result = mockMvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));

    }

    @DisplayName("deleteArticle : 블로그 글 삭제")
    @Test
    public void deleteArticle() throws Exception{

        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = createDefalutArticle();

        mockMvc.perform(
                delete(url, savedArticle.getId())
                        ).andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();

    }


    @DisplayName("updateArticle : 블로그 글 수정")
    @Test
    public void updateArticle() throws Exception{

        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        Article savedArticle = createDefalutArticle();


        final String updateTitle = "title1";
        final String updateContent = "content1";


        Article updateArticle = Article.builder()
                .title(updateTitle)
                .content(updateContent)
                        .build();

        final String requestBody = objectMapper.writeValueAsString(updateArticle);


        mockMvc.perform(
                put(url, savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isOk());

        Article compareArticle = blogRepository.findById(savedArticle.getId()).orElseThrow(()-> new RuntimeException("ID를 찾을 수 없음."));
        assertThat(compareArticle.getTitle()).isEqualTo(updateTitle);
        assertThat(compareArticle.getContent()).isEqualTo(updateContent);


    }


    private Article createDefalutArticle(){
        return blogRepository.save(Article.builder()
                        .title("title")
                        .author(user.getUsername())
                        .content("content")
                .build());
    }
}