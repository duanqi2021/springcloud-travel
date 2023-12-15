package cn.dq.redis.key;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.concurrent.TimeUnit;


@Setter
@AllArgsConstructor
public class BaseKeyPrefix implements KeyPrefix{
    private String prefix;
    private Long timeout;

    private TimeUnit unit;

    public BaseKeyPrefix(String prefix){
        this(prefix,10l,TimeUnit.MINUTES);
    }
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public Long getTimeout() {
        return timeout;
    }

    @Override
    public TimeUnit getUnit() {
        return unit;
    }
}
