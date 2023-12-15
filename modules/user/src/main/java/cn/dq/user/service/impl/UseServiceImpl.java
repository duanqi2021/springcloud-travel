package cn.dq.user.service.impl;

import cn.dq.email.po.EmailModel;
import cn.dq.email.service.EmailService;
import cn.dq.excepiton.BusinessException;
import cn.dq.user.domain.Userinfo;
import cn.dq.user.mapper.UseMappser;
import cn.dq.user.service.UseService;
import cn.dq.user.utils.ValidateCodeUtils;
import cn.dq.redis.utils.RedisService;
import cn.dq.user.vo.LoginUser;
import cn.dq.user.vo.RegistUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.dq.user.rediskey.UserRedisKeyPrefix.USER_LOGIN_INFO_KEY;
import static cn.dq.user.rediskey.UserRedisKeyPrefix.USER_VERIFY_CODE_KEY;

@Service
public class UseServiceImpl extends ServiceImpl<UseMappser, Userinfo> implements UseService {
    public static final String MODE = "您正在注册 偷偷摸摸 网站用户，验证码为：%s,有效时间%s分钟，请尽快完成注册";

    private  RedisService redisService;

    private  EmailService emailService;

    UseServiceImpl(RedisService redisService,EmailService emailService){
        this.redisService=redisService;
        this.emailService=emailService;
    }

    @Value("${jwt.signkey}")
    public  String SIGN_KEY;


    @Value("${jwt.expireTime}")
    public  Long jwtExpireTime;


    @Override
    public Userinfo findOneByEmail(String email) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        return getOne(wrapper);
    }

    @Override
    public boolean getCode(String email) {
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        //存在redis里面
        redisService.setCacheObject(USER_VERIFY_CODE_KEY,code,email);
        //发送邮件
        EmailModel model=new EmailModel();
        model.setTitle("验证码");
        model.setContent(String.format(MODE,code,"10"));
        model.setRecipientMailbox(new String[]{email});
        return emailService.sendSimpleEmail(model);

    }


    @Override
    public void regist(RegistUserVo userinfo) {
        if(findOneByEmail(userinfo.getEmail())!=null){
            //抛异常
            throw new BusinessException("邮箱已注册");
        }
        //判断验证码
        if(!userinfo.getVerifyCode().equals(redisService.getCacheObject(USER_VERIFY_CODE_KEY.fullKey(userinfo.getEmail())))){
            throw new BusinessException("验证码错误");
        }
        //加密
        //转化
        save(RegistUserVo.psf(userinfo));
        //删除验证码
        redisService.deleteObject(USER_VERIFY_CODE_KEY.fullKey(userinfo.getEmail()));
    }

    @Override
    public Map<String, Object> login(String name, String password) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email",name);
        Userinfo one = getOne(wrapper);
        if(one==null){
            throw new BusinessException("用户名不存在");
        }
        if(!one.getPassword().equals(password)){
            throw new BusinessException("密码错误");
        }
        LoginUser loginUser=new LoginUser();
        BeanUtils.copyProperties(one,loginUser);
        Map<String, Object> map=new HashMap<>();
        map.put("id",one.getId());
        map.put("nickname",one.getNickname());
        //登录时间
        long nowTime=System.currentTimeMillis();
        loginUser.setLoginTime(nowTime);
        //过期时间
        long expireTime=nowTime+(jwtExpireTime*60*1000);
        loginUser.setExpireTime(expireTime);

        //设置时间
        USER_LOGIN_INFO_KEY.setTimeout(jwtExpireTime);
        USER_LOGIN_INFO_KEY.setUnit(TimeUnit.MINUTES);
        //用户过期时间存redis
        redisService.setCacheObject(USER_LOGIN_INFO_KEY,loginUser,one.getId().toString());


        final String jwtToken = Jwts.builder()
                .addClaims(map)
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .compact();
        map.clear();
        map.put("token",jwtToken);
        map.put("user",loginUser);
        return map;
    }
}
