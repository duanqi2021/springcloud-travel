package cn.dq.auth.config;


import cn.dq.auth.interceptor.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不进入拦截
        registry.addInterceptor(loginInterceptor)
        //        .excludePathPatterns("/userInfos/*")
                .addPathPatterns("/**");
    }
}
