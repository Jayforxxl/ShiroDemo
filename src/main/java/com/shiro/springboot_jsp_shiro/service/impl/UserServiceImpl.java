package com.shiro.springboot_jsp_shiro.service.impl;

import com.shiro.springboot_jsp_shiro.dao.UserDao;
import com.shiro.springboot_jsp_shiro.entity.Permission;
import com.shiro.springboot_jsp_shiro.entity.Role;
import com.shiro.springboot_jsp_shiro.entity.User;
import com.shiro.springboot_jsp_shiro.service.UserService;
import com.shiro.springboot_jsp_shiro.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public void register(User user) {
		//生成随机盐
		String salt = SaltUtils.getSalt(8);
		user.setSalt(salt);
		//明文密码进行md5+salt+hash散列
		Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
		user.setPassword(md5Hash.toHex());
		userDao.save(user);
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public User findRolesByUserName(String username) {
		return userDao.findRolesByUserName(username);
	}

	@Override
	public List<Permission> findPermissionsByRoleId(String roleId) {
		return userDao.findPermissionsByRoleId(roleId);
	}
}
