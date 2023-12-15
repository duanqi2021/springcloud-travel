package cn.dq.user.interceptor;

import cn.dq.excepiton.BusinessException;
import cn.dq.redis.utils.RedisService;
import cn.dq.user.vo.LoginUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static cn.dq.user.rediskey.UserRedisKeyPrefix.USER_LOGIN_INFO_KEY;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${jwt.signkey}")
    private  String SIGN_KEY;


    @Value("${jwt.expireTime}")
    public  Long jwtExpireTime;

    private RedisService redisService;

    LoginInterceptor(RedisService redisService){
        this.redisService=redisService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中 拿token
        final String token = request.getHeader("token");
        //解析
        try {
            final Jws<Claims> headerClaimsJwt = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token);
            final Claims body = headerClaimsJwt.getBody();
            final String id =String.valueOf(body.get("id"));
            final LoginUser user = redisService.getCacheObject(USER_LOGIN_INFO_KEY.fullKey(id));
            long nowTime=System.currentTimeMillis();
            if(user==null){
                throw new BusinessException(401,"Token已经过期");
            } else if (user.getExpireTime()-nowTime<=20*60*1000) {
                //用户过期时间小于20分钟还在使用则续期用户的登录时间
                user.setLoginTime(nowTime);
                long expireTime=nowTime+(jwtExpireTime*60*1000);
                user.setExpireTime(expireTime);
                USER_LOGIN_INFO_KEY.setTimeout(expireTime);
                USER_LOGIN_INFO_KEY.setUnit(TimeUnit.MILLISECONDS);
                //用户过期时间存redis
                redisService.setCacheObject(USER_LOGIN_INFO_KEY,user,user.getId().toString());
            }

        }catch (Exception e){
            throw new BusinessException("用户未认证");
        }
        return true;
    }
}
