package cn.dq.user.rediskey;

import cn.dq.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {
    public final static UserRedisKeyPrefix KEY=new UserRedisKeyPrefix("USERS:REGIST:VERIFY_CODE");
    public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }
}
