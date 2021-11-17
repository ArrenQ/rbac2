package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.RoleAbility;
import com.chuang.rbac2.crud.mapper.RoleAbilityMapper;
import com.chuang.rbac2.crud.service.IAbilityService;
import com.chuang.rbac2.crud.service.IRoleAbilityService;
import com.chuang.rbac2.crud.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleAbilityServiceImpl extends ServiceImpl<RoleAbilityMapper, RoleAbility> implements IRoleAbilityService {

    @Resource private IRoleService roleService;
    @Resource private IAbilityService abilityService;

    @Override
    public boolean addAdminRoleAbility(Integer abilityId) {
        return roleService.findByCode("admin").map(role -> {
            RoleAbility ra = new RoleAbility()
                    .setAbilityId(abilityId)
                    .setRoleId(role.getId())
                    .setLicensable(true);
            return save(ra);
        }).orElse(false);
    }

    @Override
    public boolean resetAdminRoleAbility() {


        return roleService.findByCode("admin").map(role -> {
            this.lambdaUpdate().eq(RoleAbility::getRoleId, role.getId()).remove();

            List<RoleAbility> list = abilityService.list().stream().map(ability -> {
                RoleAbility ra = new RoleAbility();
                ra.setRoleId(role.getId());
                ra.setAbilityId(ability.getId());
                ra.setLicensable(true);
                return ra;
            }).collect(Collectors.toList());
            return saveBatch(list);
        }).orElse(false);
    }
}
