package cn.dq.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.dq.user.mapper")
public class UseApplication {
    public static void main(String[] args) {
        SpringApplication.run(UseApplication.class,args);
    }
}
