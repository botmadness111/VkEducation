package ru.andrey.VkEducation.inMemoryCash;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class InMemoryCashConfiguration {
    @Bean
    public Jedis jedis() {
        return new Jedis("localhost", 6379);
    }
}
