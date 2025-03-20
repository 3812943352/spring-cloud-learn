/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-12 09:13:30
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 10:05:35
 * @FilePath: common-module/src/main/java/com/common/commonmodule/config/RedisTemplateConfig.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.common.commonmodule.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisTemplateConfig {

    @Resource
    private RedisConnectionFactory factory;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setShareNativeConnection(true);
        factory.setValidateConnection(true);
        factory.setShutdownTimeout(5000);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 配置ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new JavaTimeModule());

        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
        GenericJackson2JsonRedisSerializer jacksonSeial = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringSerial = new StringRedisSerializer();

        template.setKeySerializer(stringSerial);
        template.setValueSerializer(jacksonSeial);
        template.setHashKeySerializer(stringSerial);
        template.setHashValueSerializer(jacksonSeial);

        template.afterPropertiesSet();

        return template;
    }
}