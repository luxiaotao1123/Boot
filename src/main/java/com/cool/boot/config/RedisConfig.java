package com.cool.boot.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @Auth Vincent
 */
@Configuration
@Order(1)
public class RedisConfig {

    @Bean("hashTemplate")
    public HashOperations<String,String,Object> createSnatchUserRedis(){
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }
    @Bean("zSetRedisTemplate")
    public ZSetOperations<String,String> createUndoneRedis(){
        return createTemplateCache(OperateEnum.STRING).opsForZSet();
    }
    @Bean("redisRedisTemplate")
    public RedisTemplate<String, String> createRemoveRedis() {
        return createTemplateCache(OperateEnum.STRING);
    }

    @Bean("myValueOperations")
    public ValueOperations<String,String> createQrcodeRedis(){
        return createTemplateCache(OperateEnum.STRING).opsForValue();
    }

    @Bean("tokenValueRedisTemplate")
    public ValueOperations<String, String> createTokenValueRedisTemplate(){
        return createTemplateCache(OperateEnum.STRING).opsForValue();
    }

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean("jedisConnectionFactory")
    public JedisConnectionFactory  createJedisConnectionFactory(@Value("${redis.host}") String host,
                                                                @Value("${redis.port}") Integer port,
                                                                @Value("${redis.database}") Integer database,
                                                                @Value("${redis.password}") String pass) {

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setDatabase(database);
        factory.setPassword(pass);
        return factory;

    }


    private enum OperateEnum {
        STRING, HASH
    }


    private RedisTemplate createTemplateCache(OperateEnum operateEnum) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
//        template.expire("",1, TimeUnit.DAYS);
        switch (operateEnum) {
            case HASH:
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
                break;
            default:
                template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        }
        template.afterPropertiesSet();
        return template;
    }


}
