package com.chuang.rbac2.crud.service.impl;

import com.chuang.rbac2.crud.entity.Ability;
import com.chuang.rbac2.crud.entity.LicensableAbility;
import com.chuang.rbac2.crud.entity.RoleAbility;
import com.chuang.rbac2.crud.entity.bo.AL;
import com.chuang.rbac2.crud.entity.bo.FullRoleAbilityBO;
import com.chuang.rbac2.crud.service.IAbilityService;
import com.chuang.rbac2.crud.service.IAssignService;
import com.chuang.rbac2.crud.service.IRoleAbilityService;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ath
 */
@Service
public class AssignServiceImpl implements IAssignService {

    @Resource private IRoleAbilityService roleAbilityService;
    @Resource private IAbilityService abilityService;


    @Override
    public List<FullRoleAbilityBO> fullRoleAbilities(String username, Integer roleId) {
        List<Ability> abilities = abilityService.list();
        List<Integer> userCanOpts = abilityService.findByUsername(username).stream()
                .filter(LicensableAbility::getLicensable)
                .map(LicensableAbility::getId)
                .collect(Collectors.toList());

        Map<Integer, LicensableAbility> roleLas = abilityService.findByRoleId(roleId).stream()
                .collect(Collectors.toMap(LicensableAbility::getId, o -> o));

        return abilities.stream().map(ability -> {
            FullRoleAbilityBO bo = ConvertKit.toBean(ability, FullRoleAbilityBO::new);
            bo.setDisableOpt(!userCanOpts.contains(bo.getId()));
            bo.setChecked(roleLas.containsKey(ability.getId()));

            boolean licensable = Optional.ofNullable(roleLas.get(ability.getId()))
                    .map(LicensableAbility::getLicensable)
                    .orElse(false);
            bo.setLicensable(licensable);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean assign(String username, Integer roleId, List<AL> abilities) {
        List<Integer> userCanOpts = abilityService.findByUsername(username).stream()
                .filter(LicensableAbility::getLicensable)
                .map(LicensableAbility::getId)
                .collect(Collectors.toList());

        Set<Integer> allCanOptId = new HashSet<>();

        List<AL> checked = abilities.stream()
                // 只保留当前用户可操作的权限（防止前端恶意参数）。
                .filter(al -> userCanOpts.contains(al.getAbilityId()))
                //所有用户可操作的权限id
                .peek(al -> allCanOptId.add(al.getAbilityId()))
                .collect(Collectors.groupingBy(AL::getChecked))
                .get(true);


        // 移除所有可操作的权限。
        roleAbilityService.lambdaUpdate()
                .eq(RoleAbility::getRoleId, roleId)
                .in(RoleAbility::getAbilityId, allCanOptId)
                .remove();

        //重新加入选中的权限
        List<RoleAbility> list = checked.stream()
                .map(al -> new RoleAbility().setRoleId(roleId).setAbilityId(al.getAbilityId()).setLicensable(al.getLicensable()))
                .collect(Collectors.toList());

        return roleAbilityService.saveBatch(list);
    }
}
