package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Role;
import com.chuang.rbac2.crud.service.IAbilityService;
import com.chuang.rbac2.crud.service.IAssignService;
import com.chuang.rbac2.crud.service.IRoleService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.co.RoleAssignCO;
import com.chuang.rbac2.rest.model.co.RoleCO;
import com.chuang.rbac2.rest.model.ro.FullRoleAbilityRO;
import com.chuang.rbac2.rest.model.ro.LicensableAbilityRO;
import com.chuang.rbac2.rest.model.ro.RoleRO;
import com.chuang.rbac2.rest.model.uo.RoleUO;
import com.chuang.tauceti.rowquery.controller.basic.ICrudController;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表;角色 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-07
 */
@RestController
@RequestMapping("/role")
public class RoleController implements ICrudController<RoleCO, RoleRO, RoleUO, Role, IRoleService> {

    @Resource private IRoleService service;
    @Resource private IAbilityService abilityService;
    @Resource private IAssignService assignService;

    @RequiresPermissions("ability:query")
    @GetMapping("/{roleId}/abilities")
    public Result<List<LicensableAbilityRO>> abilities(@PathVariable Integer roleId) {
        return Result.success(abilityService.findByRoleId(roleId))
                .map(abilities -> ConvertKit.convert(abilities, ArrayList::new, LicensableAbilityRO::new));
    }

    @RequiresPermissions(value = {"ability:query", "role:assign"}, logical = Logical.OR)
    @GetMapping("/{roleId}/abilities/full")
    @ApiOperation("根据roleId获取角色包含的所有权限")
    public Result<List<FullRoleAbilityRO>> fullRoleAbilities(@PathVariable @Valid Integer roleId) {
        return Result.success(assignService.fullRoleAbilities(OfficeUtils.shiroUserNotNull().getUsername(), roleId))
                .map(roleAbilityBos -> ConvertKit.toBeans(roleAbilityBos, FullRoleAbilityRO::new));
    }

    @GetMapping("/menus")
    public Result<List<RoleRO>> menusRole() {
        List<Role> list = service.lambdaQuery().list();
        return Result.success(list)
                .map(roles -> ConvertKit.toBeans(roles, RoleRO::new));
    }


    @RequiresPermissions("role:assign")
    @PostMapping("/assign")
    public Result<Boolean> assign(@RequestBody @ApiParam @Valid RoleAssignCO roleAssign) {
        return Result.success(
                assignService.assign(
                        OfficeUtils.shiroUserNotNull().getUsername(),
                        roleAssign.getRoleId(),
                        roleAssign.getAbilities()
                )
        );
    }

    @Override
    public IRoleService service() {
        return service;
    }


    @Override
    public String basePermission() {
        return "role:";
    }

}

