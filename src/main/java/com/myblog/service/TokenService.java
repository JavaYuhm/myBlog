package com.myblog.service;

import com.myblog.config.TokenProvider;
import com.myblog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken){

        // 리프레쉬 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.vaildToken(refreshToken)){
            throw  new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findByid(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
