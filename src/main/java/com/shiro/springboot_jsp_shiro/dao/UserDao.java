package com.shiro.springboot_jsp_shiro.dao;

import com.shiro.springboot_jsp_shiro.entity.Permission;
import com.shiro.springboot_jsp_shiro.entity.Role;
import com.shiro.springboot_jsp_shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    void save(User user);

    User findByUserName(String username);

    User findRolesByUserName(String username);

    List<Permission> findPermissionsByRoleId(String roleId);
}
