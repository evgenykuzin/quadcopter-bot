package ru.jekajops.quadcopterbot.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisSentinelConfiguration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import ru.jekajops.quadcopterbot.models.Cart;
//
//import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

@Configuration
public class AppConfiguration {
    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    /**
     * Jedis
     */
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }

//    /**
//     * Lettuce
//     */
//    @Bean
//    public RedisConnectionFactory lettuceConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("127.0.0.1", 26379)
//                .sentinel("127.0.0.1", 26380);
//        return new LettuceConnectionFactory(sentinelConfig);
//    }

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//
//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .readFrom(REPLICA_PREFERRED)
//                .build();
//
//        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration("server", 6379);
//
//        return new LettuceConnectionFactory(serverConfig, clientConfig);
//    }
//
//    @Bean
//    public RedisTemplate<Long, Cart> redisTemplate() {
//        RedisTemplate<Long, Cart> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        return template;
//    }
}
