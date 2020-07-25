package com.shiro.springboot_jsp_shiro.config;

import com.shiro.springboot_jsp_shiro.shiro.cache.RedisCacheManager;
import com.shiro.springboot_jsp_shiro.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:整合Shiro框架的配置类
 *
 * @author ZhangJieChao
 * @version 1.0
 * @date 2020/7/21 22:44
 */

@Configuration
public class ShiroConfig {

    /**
     * 创建ShiroFilter
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //配置系统公共资源和系统受限资源
        Map<String,String> map = new HashMap<>();
        map.put("/user/login","anon");//anon设置为公共资源(放行资源必须放于认证资源的上面)
        map.put("/register.jsp","anon");
        map.put("/user/register","anon");
        map.put("/user/getImage","anon");
        map.put("/**","authc");//authc表示这个需要认证和授权
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        //默认认证界面路径也是login.jsp,这里可以不写
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");

        return shiroFilterFactoryBean;
    }

    /**
     * 创建安全管理器
     * @param realm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置Realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    /**
     * 创建自定义Realm
     * @return
     */
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        //修改凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        //开启缓存管理(不会频繁访问数据库,访问本地缓存)
        customerRealm.setCacheManager(new RedisCacheManager());
        customerRealm.setCachingEnabled(true);//开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true);//开启认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }

}
