package com.dealsky.dao;

import com.dealsky.vo.User;

import java.util.List;

/**
 * @Author: dealsky
 * @Date: 2018/5/8 17:47
 */
public interface UserDao {
    User getUserByUserName(String userName);

    List<String> queryRolesByUserName(String username);

    List<String> queryPermissionByRoleName(String roleName);
}
