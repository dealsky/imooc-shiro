package com.dealsky.shiro.realm;

import com.dealsky.dao.UserDao;
import com.dealsky.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: dealsky
 * @Date: 2018/5/7 21:18
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    private Map<String, String> userMap = new HashMap<>(16);

    {
        userMap.put("dealsky", "2cb498d4f03459ae82099d2c10d8c89a");

        super.setName("customRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userName = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByUserName(userName);

        List<String> permissions = new ArrayList<>();

        for (String role : roles) {
            List<String> list = getPermissionsByUserName(role);
            permissions.addAll(list);
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(new HashSet<>(permissions));

        return simpleAuthorizationInfo;
    }

    private List<String> getPermissionsByUserName(String roleName) {
        return userDao.queryPermissionByRoleName(roleName);
    }

    private Set<String> getRolesByUserName(String userName) {
        System.out.println("从数据库中获取授权信息");
        List<String> list = userDao.queryRolesByUserName(userName);
        return new HashSet<>(list);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.从主体过来的用户信息中获取用户名
        String userName = (String) authenticationToken.getPrincipal();

        // 2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, this.getName());
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "dealsky");
        System.out.println(md5Hash.toString());
    }
}
