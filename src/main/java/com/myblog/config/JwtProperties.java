package com.myblog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ConfigurationProperties("jwt")
@Configuration
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
