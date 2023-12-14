package cn.dq.email.config;

import cn.dq.email.service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyEmailAutoConfiguration {
    @Bean
    public EmailService emailService(){
        return new EmailService();
    }

}
