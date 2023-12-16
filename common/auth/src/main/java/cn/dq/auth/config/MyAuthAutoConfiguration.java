package cn.dq.auth.config;

import cn.dq.auth.interceptor.LoginInterceptor;
import cn.dq.redis.utils.RedisService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(JwtProperties.class)
@Import(WebConfig.class)
@Configuration
public class MyAuthAutoConfiguration {
    @Bean
    public LoginInterceptor loginInterceptor(RedisService redisService,JwtProperties jwtProperties){
        return new LoginInterceptor(jwtProperties,redisService);
    }

}
