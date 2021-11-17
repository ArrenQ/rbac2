package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Role;
import com.chuang.rbac2.crud.enums.RoleType;
import com.chuang.tauceti.rowquery.IRowQueryService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 角色表;角色 服务类
 * </p>
 *
 * @author chuang
 * @since 2021-05-07
 */
public interface IRoleService extends IRowQueryService<Role> {

    /**
     * 根据用户名查找用户拥有的所有角色
     * @param username 用户名
     * @return 所有角色。注意：是所有类型的角色，包含用户角色，组织角色，职位角色
     */
    Collection<Role> findAllRoles(String username);

    /**
     * 根据用户名查找用户拥有的用户角色
     * @param username 用户名
     * @return 所有用户角色。注意：仅用户角色
     */
    List<Role> findUserRoles(String username);

    /**
     * 查找所有用户类型的角色
     * @return 所有用户角色。注意：仅用户角色
     */
    default List<Role> findUserRoles() {
        return lambdaQuery().eq(Role::getRoleType, RoleType.USER_ROLE).list();
    }

    /**
     * 根据角色编号获取角色
     * @param roleCode 角色编号
     * @return 角色
     */
    default Optional<Role> findByCode(String roleCode) {
        return lambdaQuery().eq(Role::getRole, roleCode).oneOpt();
    }

}
