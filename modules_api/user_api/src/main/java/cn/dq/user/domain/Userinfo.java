package cn.dq.user.domain;

import cn.dq.user.handler.EncryptHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "userinfo",autoResultMap = true)
public class Userinfo implements Serializable {

    private static final long serialVersionUID=1L;

    public static  final  int GENDER_SECRET =0;//保密
    public static  final  int GENDER_MALE =1;//男
    public static  final  int GENDER_FEMALE =2;//女
    public static  final  int STATE_NORMAL =0;//正常

    public static  final  int STATE_DISABLE =1;//冻结


    @TableId(type= IdType.AUTO)
    private  Long id;
    private String nickname;

    private String phone;

    private String email;
    @TableField(typeHandler= EncryptHandler.class)
    @JsonIgnore
    private String password;
    private Integer gender =GENDER_SECRET;
    private Integer level=0;
    private String city;
    private String headImgUrl;

    private String info;

    private Integer state = STATE_DISABLE;

}
