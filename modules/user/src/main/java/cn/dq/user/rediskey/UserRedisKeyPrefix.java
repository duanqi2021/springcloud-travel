package cn.dq.user.rediskey;

import cn.dq.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {
    public final static UserRedisKeyPrefix USER_VERIFY_CODE_KEY=new UserRedisKeyPrefix("USERS:REGIST:VERIFY_CODE",10l,TimeUnit.MINUTES);

    public final static UserRedisKeyPrefix USER_LOGIN_INFO_KEY=new UserRedisKeyPrefix("USERS:LOGIN:USER_INFO");
    public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }

}
