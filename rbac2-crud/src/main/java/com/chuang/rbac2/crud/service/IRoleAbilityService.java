package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.RoleAbility;
import com.chuang.tauceti.rowquery.IRowQueryService;

/**
 * 角色权限服务
 * @author ath
 */
public interface IRoleAbilityService extends IRowQueryService<RoleAbility> {

    /**
     * 给admin的角色添加权限，系统每次创建一个权限都会调用这个方法来给admin加权。
     * @param abilityId 权限ID
     * @return 是否添加成功
     */
    boolean addAdminRoleAbility(Integer abilityId);

    boolean resetAdminRoleAbility();

}
