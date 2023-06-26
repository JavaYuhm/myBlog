package com.myblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREPIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키 값 조회
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 접두사 제거(Bearer) 후 유효한 토큰일때 인증정보 설정
        String token = getAccessToken(authHeader);
        if(tokenProvider.vaildToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authHeader) {
        if(authHeader != null && authHeader.startsWith(TOKEN_PREPIX)){
            return authHeader.substring(TOKEN_PREPIX.length());
        }
        return null;
    }
}
