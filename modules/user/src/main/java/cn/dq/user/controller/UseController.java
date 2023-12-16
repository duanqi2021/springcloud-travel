package cn.dq.user.controller;


import cn.dq.auth.anno.RequireLogin;
import cn.dq.user.service.impl.UseServiceImpl;
import cn.dq.user.vo.RegistUserVo;
import cn.dq.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;




@RestController
@RequestMapping("/userInfos")
@AllArgsConstructor
public class UseController {
    private final UseServiceImpl useService;


    @RequireLogin
    @GetMapping("/checkEmail")
    public R<Boolean> checkEmailExists(@RequestParam("email") String email){
        return R.ok(useService.findOneByEmail(email)!=null);
    }
    @GetMapping ("/sendVerifyCode")
    public R<Boolean>  sendAndGetVerifyCode(@RequestParam("email") String email){
        return R.ok(useService.getCode(email));
    }


    @PostMapping("/regist")
    public R<Boolean>  registUser(@Valid RegistUserVo registUserVo){
        useService.regist(registUserVo);
        return R.ok();
    }

    @PostMapping("/login")
    public R<?>  login(String username,String password){
        return R.ok(useService.login(username,password));
    }



}
