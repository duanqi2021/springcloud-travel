package cn.dq.user.service.impl;

import cn.dq.excepiton.BusinessException;
import cn.dq.user.domain.Userinfo;
import cn.dq.user.mapper.UseMappser;
import cn.dq.user.service.UseService;
import cn.dq.user.utils.MailUtils;
import cn.dq.user.utils.ValidateCodeUtils;
import cn.dq.redis.utils.RedisService;
import cn.dq.user.vo.RegistUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UseServiceImpl extends ServiceImpl<UseMappser, Userinfo> implements UseService {

    private final RedisService redisService;

    private final static String KEY="USERS:REGIST:VERIFY_CODE";
    @Override
    public Userinfo findOneByEmail(String email) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        return getOne(wrapper);
    }

    @Override
    public void getCode(String email) {
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        //存在redis里面
        redisService.setCacheObject(KEY+email,code,10L, TimeUnit.MINUTES);
        //发送邮件
        MailUtils.sendMail(email,String.format(MailUtils.MODE,code,"10"),"验证码");

    }


    @Override
    public void regist(RegistUserVo userinfo) {
        if(findOneByEmail(userinfo.getEmail())!=null){
            //抛异常
            throw new BusinessException("邮箱已注册");
        }
        //判断验证码
        if(!userinfo.getVerifyCode().equals(redisService.getCacheObject(KEY+userinfo.getEmail()))){
            throw new BusinessException("验证码错误");
        }
        //加密
        //转化

        save(RegistUserVo.psf(userinfo));
        //删除验证码
        redisService.deleteObject(KEY+userinfo.getEmail());
    }
}
