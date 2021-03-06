package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.User;
import com.chuang.tauceti.rowquery.IRowQueryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户  服务类
 * </p>
 *
 * @author chuang
 * @since 2021-05-21
 */
public interface IUserService extends IRowQueryService<User> {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户
     */
    default Optional<User> findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername,username).oneOpt();
    }

    /**
     * 给用户重置角色
     * @param username 用户名
     * @param roles 角色编号 集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean assignRole(String username, List<String> roles);

    /**
     * 重置用户加入的组织列表、
     * @param operator 操作者
     * @param username 用户名
     * @param organizationCodes 组织编号集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean resetJoinGroup(String operator, String username, List<String> organizationCodes);

    /**
     * 重置用户职位列表
     * @param username 用户名
     * @param positionCodes 职位编号集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    boolean  resetAppointment(String username, List<String> positionCodes);

    /**
     * 根据用户名删除用户
     * @param username 用户名
     * @return 是否删除成功
     */
    default boolean deleteByUsername(String username) {
        return lambdaUpdate().eq(User::getUsername, username).remove();
    }

}
