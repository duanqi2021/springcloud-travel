package cn.dq.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private  Long id;
    private String nickname;

    private String phone;

    private String email;
    private Integer gender ;
    private Integer level;
    private String city;
    private String headImgUrl;

    private String info;
    private Long loginTime;
    private Long expireTime;
}
