package com.shiro.springboot_jsp_shiro.shiro.realms;

import com.shiro.springboot_jsp_shiro.entity.Permission;
import com.shiro.springboot_jsp_shiro.entity.Role;
import com.shiro.springboot_jsp_shiro.entity.User;
import com.shiro.springboot_jsp_shiro.salt.MyByteSource;
import com.shiro.springboot_jsp_shiro.service.UserService;
import com.shiro.springboot_jsp_shiro.utils.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

//自定义Realm
public class CustomerRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        String primaryPrincipal = (String)principal.getPrimaryPrincipal();
        UserService userService = (UserService)ApplicationContextUtils.getBeanByName("userServiceImpl");
        User user = userService.findRolesByUserName(primaryPrincipal);
        if (!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role -> {
                //配置角色信息
                simpleAuthorizationInfo.addRole(role.getName());
                //配置权限信息
                List<Permission> permissions = userService.findPermissionsByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(permissions)){
                    permissions.forEach(permission -> {
                        simpleAuthorizationInfo.addStringPermission(permission.getName());//权限字符串

                    });
                }

            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String)token.getPrincipal();

        UserService userServiceImpl = (UserService)ApplicationContextUtils.getBeanByName("userServiceImpl");
        User user = userServiceImpl.findByUserName(principal);
        if (!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(principal,user.getPassword(),
                    new MyByteSource(user.getSalt()),this.getName());
        }
        return null;
    }
}
