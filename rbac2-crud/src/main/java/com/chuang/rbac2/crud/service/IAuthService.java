package com.chuang.rbac2.crud.service;


import com.chuang.tauceti.support.BiValue;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * Auth服务
 * @author ath
 */
public interface IAuthService {

    /**
     * 创建一个用户
     * @param username 用户账号
     * @param name 用户昵称
     * @param ipBound 绑定的IP，一旦设定，用户只能通过这个IP进行登录
     * @param macBound 绑定的MAC，一旦设定，用户只能通过这个MAC地址进行登录
     * @return 随机生成的密码
     */
    @Transactional(rollbackFor = Exception.class)
    Optional<String> createUser(String username, String name, @Nullable String ipBound, @Nullable String macBound);

    /**
     * 强制更新密码
     * @param username 用户名
     * @param password 新密码
     * @return 是否更新成功
     */
    boolean forceChangePassword(String username, String password);

    /**
     * 更新密码
     * @param username 用户名
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * 根据用户名获取用户所有的 角色 和 权限 对象
     * @param username 用户名
     * @return 角色和权限结合
     */
    BiValue<Set<String>, Set<String>> roleAndAbilities(String username);
}
