package cn.dq.user.controller;

import cn.dq.user.domain.Userinfo;
import cn.dq.user.service.impl.UseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UseController {
    private final UseServiceImpl useService;

    @GetMapping
    public List<Userinfo> get(){
        return useService.list();
    }

}
