package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Ability;
import com.chuang.rbac2.crud.entity.LicensableAbility;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 权限表 权限 服务类
 * </p>
 *
 * @author chuang
 * @since 2021-01-05
 */
public interface IAbilityService extends ITreeService<Ability> {

    List<LicensableAbility> findByUsername(String username);

    List<LicensableAbility> findByRoleId(Integer roleId);

    List<LicensableAbility> findByRoleIds(List<Integer> roleIds);

    default Optional<Ability> findByAbility(String ability) {
        return lambdaQuery().eq(Ability::getAbility, ability).list().stream().findFirst();
    }
}
