package cn.dq.user.controller;


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



}
