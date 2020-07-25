package com.shiro.springboot_jsp_shiro.shiro.cache;

import com.shiro.springboot_jsp_shiro.utils.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

//自定义Redis缓存的实现
public class RedisCache<k,v> implements Cache<k,v> {

    private String cacheName;

    public RedisCache() {
    }

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public v get(k k) throws CacheException {
        RedisTemplate redisTemplate = (RedisTemplate)ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return (v)redisTemplate.opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        //将数据库中查询出来的认证和授权的信息存入到Redis中(从下次可以从上面的get方法中直接获取信息而不是去数据库中获取)
        //principals + credential + credentialsSalt
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForHash().put(this.cacheName,k.toString(),v);
        // 在Redis中会添加授权+认证两条信息:
        // PS:添加的每条信息由于opsForHash()的原因所以添加数据的数据结构为:Map<String,Map<String,Object>>
        // "authenticationCache"(里面可以包含多个人的认证信息)
        // "com.shiro.springboot_jsp_shiro.shiro.realms.CustomerRealm.authorizationCache"(里面可以包含多个人的授权信息)
        return null;
    }

    //当用户退出登录的时候会触发删除
    @Override
    public v remove(k k) throws CacheException {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return (v) redisTemplate.opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.delete(this.cacheName);
    }

    @Override
    public int size() {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate.opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate.opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<v> values() {
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBeanByName("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate.opsForHash().values(this.cacheName);
    }
}
