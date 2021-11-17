package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.bo.AL;
import com.chuang.rbac2.crud.entity.bo.FullRoleAbilityBO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 授权服务
 * @author ath
 */
public interface IAssignService {

    /**
     * 根据用户名和待编辑的角色id，查找所有权限对象，该权限对象包含了权限信息，以及当前用户是否可以对其进行选择和授权
     * @param username 执行授权操作的用户
     * @param roleId 需要赋权的角色id
     * @return 返回所有权限信息，每个权限信息中还包含了username代表的用户是否有权将这个权限赋权给 roleId 代表的角色
     */
    List<FullRoleAbilityBO> fullRoleAbilities(String username, Integer roleId);

    @Transactional(rollbackFor = Throwable.class)
    Boolean assign(String username, Integer roleId, List<AL> abilities);
}
