package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Ability;
import com.chuang.rbac2.crud.entity.Menu;
import com.chuang.rbac2.crud.enums.AbilityType;
import com.chuang.rbac2.crud.enums.I18nType;
import com.chuang.rbac2.crud.enums.Language;
import com.chuang.rbac2.crud.service.IAbilityService;
import com.chuang.rbac2.crud.service.II18nService;
import com.chuang.rbac2.crud.service.IMenuService;
import com.chuang.rbac2.crud.service.IRoleAbilityService;
import com.chuang.rbac2.rest.model.ModuleInitParams;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.support.exception.BusinessException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Resource private IMenuService menuService;
    @Resource private IAbilityService abilityService;
    @Resource private II18nService ii18nService;
    @Resource private IRoleAbilityService roleAbilityService;

    @PostMapping("/module/init")
    @RequiresRoles("admin")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> initModule(@RequestBody ModuleInitParams params) {

        initAbility(params);
        initMenu(params);
        initI18n(params);
        roleAbilityService.resetAdminRoleAbility();
        return Result.success();
    }

    @PostMapping("/ability/reset")
    @RequiresRoles("admin")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> resetAbility() {
        roleAbilityService.resetAdminRoleAbility();
        return Result.success();
    }

    private void initI18n(ModuleInitParams params) {
        ii18nService.save(I18nType.CLIENT, "menu." + params.getCode(), params.getName(), Language.zh_CN);
        ii18nService.save(I18nType.CLIENT, "menu." + params.getCode(), params.getName(), Language.zh_TW);
        ii18nService.save(I18nType.CLIENT, "menu." + params.getCode(), params.getCode(), Language.en_US);
    }

    private void initMenu(ModuleInitParams params) {
        Menu parentMenu = menuService.findByCoe(params.getParentMenu())
                .orElseThrow(() -> new BusinessException("无法根据菜单编号{0}找到指定菜单", params.getParentMenu()));

        Menu menu = new Menu();
        menu.setEnabled(true);
        menu.setCode("menu-" + params.getCode());
        menu.setParents(parentMenu.getParents() + parentMenu.getId() + "/");
        menu.setParentId(parentMenu.getId());
        menu.setHideInBreadcrumb(true);
        menu.setI18n("menu." + params.getCode());
        menu.setIcon("");
        menu.setReuse(true);
        menu.setShortcutRoot(false);
        menu.setLink(params.getRoutePath());
        menu.setExternalLink("");
        menu.setTarget("_self");
        menu.setAcl("{\"ability\":[\"" + params.getAbilityPrefix() + "page\"]}");
        menu.setSortRank(100);
        menu.setText(params.getName());
        menuService.save(menu);
    }

    private void initAbility(ModuleInitParams params) {
        Ability parentAbility = abilityService.findByAbility(params.getParentAbility())
                .orElseThrow(() -> new BusinessException("无法找到{0}权限", params.getParentAbility()));

        Ability ability = new Ability();
        ability.setAbility(params.getAbilityPrefix() + "page");
        ability.setAbilityType(AbilityType.MENU);
        ability.setDescription("admin gen");
        ability.setEnabled(true);
        ability.setName(params.getName());
        ability.setParentId(parentAbility.getId());
        ability.setParents(parentAbility.getParents() + parentAbility.getId() + "/");
        ability.setSortRank(100);
        abilityService.save(ability);

        Ability abilityChild = new Ability();
        abilityChild.setAbility(params.getAbilityPrefix() + "query");
        abilityChild.setAbilityType(AbilityType.FUNCTION);
        abilityChild.setName(params.getName() + "-查询");
        abilityChild.setDescription("admin gen");
        abilityChild.setEnabled(true);
        abilityChild.setParentId(ability.getId());
        abilityChild.setParents(ability.getParents() + ability.getId() + "/");
        abilityChild.setSortRank(0);
        abilityService.save(abilityChild);

        abilityChild.setAbility(params.getAbilityPrefix() + "create");
        abilityChild.setName(params.getName() + "-创建");
        abilityChild.setSortRank(1);
        abilityChild.setId(null);
        abilityService.save(abilityChild);

        abilityChild.setAbility(params.getAbilityPrefix() + "update");
        abilityChild.setName(params.getName() + "-修改");
        abilityChild.setSortRank(2);
        abilityChild.setId(null);
        abilityService.save(abilityChild);

        abilityChild.setAbility(params.getAbilityPrefix() + "delete");
        abilityChild.setAbilityType(AbilityType.FUNCTION);
        abilityChild.setName(params.getName() + "-删除");
        abilityChild.setSortRank(3);
        abilityChild.setId(null);
        abilityService.save(abilityChild);
    }
}
