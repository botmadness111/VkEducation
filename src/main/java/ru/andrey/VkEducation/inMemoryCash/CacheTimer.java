package ru.andrey.VkEducation.inMemoryCash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class CacheTimer {
    private final Jedis jedis;

    @Autowired
    public CacheTimer(Jedis jedis) {
        this.jedis = jedis;
    }

    private void setExpiration(String key, int seconds) {
        jedis.expire(key, seconds);
    }

    public void keyCreatedOrAccessed(String key) {
        setExpiration(key, 30 * 60); // время жизни кэша 30 минут если
    }
}
