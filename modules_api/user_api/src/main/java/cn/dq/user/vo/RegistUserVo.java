package cn.dq.user.vo;


import cn.dq.user.domain.Userinfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;

@Data
public class RegistUserVo {

    @Email(message = "Invalid email address")
    private String email;
    private String password;

    private String nickname;

    private String verifyCode;

    public static Userinfo psf(RegistUserVo vo){
        Userinfo userinfo=new Userinfo();
        BeanUtils.copyProperties(vo,userinfo);
        userinfo.setState(Userinfo.STATE_NORMAL);
        return userinfo;
    }
}
