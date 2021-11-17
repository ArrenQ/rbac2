package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.UserInfo;
import com.chuang.tauceti.rowquery.IRowQueryService;

import java.util.Optional;

/**
 * <p>
 * 用户信息  服务类
 * </p>
 *
 * @author chuang
 * @since 2020-12-20
 */
public interface IUserInfoService extends IRowQueryService<UserInfo> {

    /**
     * 根据用户名查找用户信息
     * @param username 用户名
     * @return 用户信息
     */
    default Optional<UserInfo> findByUsername(String username) {
        return lambdaQuery().eq(UserInfo::getUsername, username).oneOpt();
    }

    /**
     * 根据用户名删除用户信息
     * @param username 用户名
     * @return 是否删除成功
     */
    default boolean deleteByUsername(String username) {
        return lambdaUpdate().eq(UserInfo::getUsername, username).remove();
    }
}
