package cn.dq.auth.interceptor;

import cn.dq.auth.anno.RequireLogin;
import cn.dq.auth.config.JwtProperties;
import cn.dq.auth.rediskey.UserRedisKeyPrefix;
import cn.dq.excepiton.BusinessException;
import cn.dq.redis.utils.RedisService;

import cn.dq.user.vo.LoginUser;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;


@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断是否为HandlerMethod实例否则放行
        if(!(handler instanceof HandlerMethod)){
            //静态资源
            //CORS域请求
            return true;
        }
        HandlerMethod hm=(HandlerMethod)handler;
        //获取controller对象

        Class<?> controller = hm.getBeanType();
        //从controller和方法上判断是否含有注解 没有放行
        RequireLogin classAnno=controller.getAnnotation(RequireLogin.class);
        RequireLogin methodAnno=hm.getMethodAnnotation(RequireLogin.class);
        if(classAnno==null&&methodAnno==null){
            return true;
        }
        //从请求头中 拿token
        final String token = request.getHeader("token");
        //解析
        try {
            final Jws<Claims> headerClaimsJwt = Jwts.parser().setSigningKey(jwtProperties.getSignkey()).parseClaimsJws(token);
            final Claims body = headerClaimsJwt.getBody();
            final String uuid = (String) body.get("uuid");
            final LoginUser user = redisService.getCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_KEY.fullKey(uuid));
            long nowTime=System.currentTimeMillis();
            if(user==null){
                throw new BusinessException(401,"Token已经过期");
            } else if (user.getExpireTime()-nowTime<=20*60*1000) {
                //用户过期时间小于20分钟还在使用则续期用户的登录时间
                user.setLoginTime(nowTime);
                long expireTime=nowTime+(jwtProperties.getExpireTime()*60*1000);
                user.setExpireTime(expireTime);
                UserRedisKeyPrefix.USER_LOGIN_INFO_KEY.setTimeout(expireTime);
                UserRedisKeyPrefix.USER_LOGIN_INFO_KEY.setUnit(TimeUnit.MILLISECONDS);
                //用户过期时间存redis
                redisService.setCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_KEY,user,user.getId().toString());
            }

        }catch (Exception e){
            throw new BusinessException("用户未认证");
        }
        return true;
    }
}
