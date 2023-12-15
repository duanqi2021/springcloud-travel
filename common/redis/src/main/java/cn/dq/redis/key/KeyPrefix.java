package cn.dq.redis.key;

import java.util.concurrent.TimeUnit;

public interface KeyPrefix {
    String getPrefix();

    Long getTimeout();

    TimeUnit getUnit();

    default String fullKey(String... fix){
        StringBuilder sb=new StringBuilder();
        sb.append(getPrefix());

        for(String s:fix){
            sb.append(":").append(s);

        }
        return sb.toString();
    }




}
