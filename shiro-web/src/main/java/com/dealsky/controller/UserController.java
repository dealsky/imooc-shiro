package com.dealsky.controller;

import com.dealsky.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dealsky
 * @Date: 2018/5/8 14:35
 */
@RestController
public class UserController {

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST)
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            subject.login(token);
        } catch (Exception e) {
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "admin";
        }

        return "no admin";
    }

    @RequestMapping(value = "/testRole/admin", method = RequestMethod.GET)
    public String testRoleAdmin() {
        return "testRole success";
    }

    @RequestMapping(value = "/testRole/leader", method = RequestMethod.GET)
    public String testRoleLeader() {
        return "testRole success";
    }

    @RequestMapping(value = "/testPerms/update", method = RequestMethod.GET)
    public String testPermsUpdate() {
        return "testPerms success";
    }

    @RequestMapping(value = "/testRole/delete", method = RequestMethod.GET)
    public String testPermsDelete() {
        return "testPerms success";
    }
}
