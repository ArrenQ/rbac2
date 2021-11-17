package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.User;
import com.chuang.rbac2.crud.service.IAuthService;
import com.chuang.rbac2.crud.service.IRoleService;
import com.chuang.rbac2.crud.service.IUserInfoService;
import com.chuang.rbac2.crud.service.IUserService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.co.ResetAppointmentCO;
import com.chuang.rbac2.rest.model.co.ResetJoinGroupCO;
import com.chuang.rbac2.rest.model.co.UserCO;
import com.chuang.rbac2.rest.model.co.UserRoleCO;
import com.chuang.rbac2.rest.model.ro.RoleRO;
import com.chuang.rbac2.rest.model.ro.UserRO;
import com.chuang.rbac2.rest.model.uo.UserUO;
import com.chuang.tauceti.rowquery.controller.basic.IDeleteController;
import com.chuang.tauceti.rowquery.controller.basic.IRetrieveController;
import com.chuang.tauceti.rowquery.controller.basic.IUpdateController;
import com.chuang.tauceti.support.BiValue;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.support.exception.BusinessException;
import com.chuang.tauceti.tools.basic.collection.CollectionKit;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户  前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-21
 */
@RestController
@RequestMapping("/user")
public class UserController implements
        IRetrieveController<UserRO, User, IUserService>,
        IUpdateController<UserUO, User, IUserService>,
        IDeleteController<User, IUserService> {

    @Resource private IUserService service;
    @Resource private IAuthService authService;
    @Resource private IRoleService roleService;
    @Resource private IUserInfoService userInfoService;


    @RequiresPermissions("user:create")
    @PostMapping(value = "/create")
    @ApiOperation("创建一条数据")
    public Result<String> create(@RequestBody @ApiParam @Valid UserCO co ) {
        Optional<String> opt = authService.createUser(co.getUsername(), co.getName(), co.getIpBound(), co.getMacBound());
        return opt.map(Result::success).orElseGet(() -> Result.fail("创建失败"));
    }




    @Override
    @DeleteMapping(value = "/delete/{username}")
    @ApiOperation("删除以条记录")
    @RequiresPermissions("user:delete")
    public Result<Void> delete(@PathVariable("username") String username) {
        service().deleteByUsername(username);
        userInfoService.deleteByUsername(username);
        return Result.whether(true);
    }


    @PostMapping(value = "/change-pwd/current/{oldPassword}/{newPassword}")
    @ApiOperation("修改密码")
    public Result<Boolean> changePassword(@PathVariable @Valid @Size(min = 6, max = 18) String oldPassword,
                                          @PathVariable @Valid @Size(min = 6, max = 18) String newPassword) {
        return Result.whether(
                authService.changePassword(OfficeUtils.shiroUserNotNull().getUsername(), oldPassword, newPassword)
        );
    }

    @RequiresPermissions("user:change-pwd")
    @PostMapping(value = "/change-pwd/{username}/{oldPassword}/{newPassword}")
    @ApiOperation("修改密码")
    public Result<Boolean> changePassword(@PathVariable @Valid @NotEmpty String username,
                                          @PathVariable @Valid @Size(min = 6, max = 18) String oldPassword,
                                          @PathVariable @Valid @Size(min = 6, max = 18) String newPassword) {
        return Result.whether(authService.changePassword(username, oldPassword, newPassword));
    }

    @RequiresPermissions("user:force-change-pwd")
    @PostMapping(value = "/change-pwd/force/{username}/{newPassword}")
    @ApiOperation("强制修改密码")
    public Result<Boolean> forceChangePassword(@PathVariable @Valid @Size(min = 6, max = 18) String username,
                                               @PathVariable @Valid @Size(min = 6, max = 18) String newPassword) {
        return Result.whether(authService.forceChangePassword(username, newPassword));
    }

    @RequiresPermissions("user:assign-role")
    @PostMapping(value = "/assign/role")
    @ApiOperation("给用户设置角色")
    public Result<Boolean> userRoles(@RequestBody @ApiParam @Valid UserRoleCO co) {
        return Result.whether(service.assignRole(co.getUsername(), co.getRoles()));
    }

    @RequiresPermissions("user:assign-role")
    @GetMapping(value = "/roles-info")
    @ApiOperation("获取角色信息")
    public Result<BiValue<List<RoleRO>, List<RoleRO>>> userRoles(String username) {
        return Result.success(new BiValue<>(
                ConvertKit.toBeans(roleService.findUserRoles(), RoleRO::new),
                ConvertKit.toBeans(roleService.findUserRoles(username), RoleRO::new)
        ));
    }

    @RequiresPermissions("user:appointment")
    @PostMapping(value = "/appointment/reset")
    @ApiOperation("任命")
    public Result<Void> appointment(@RequestBody @ApiParam @Valid ResetAppointmentCO co) {
        return Result.whether(service.resetAppointment(co.getUsername(), CollectionKit.nullToEmpty(co.getPositionCodes())));
    }


    @RequiresPermissions("user:join-group")
    @PostMapping(value = "/join-group/reset")
    @ApiOperation("重置用户加入的组织")
    public Result<Void> resetJoinGroup(@RequestBody @ApiParam @Valid ResetJoinGroupCO co) {
        String operator = OfficeUtils.shiroUserNotNull().getUsername();
        if (co.getUsername().equalsIgnoreCase(operator)) {
            throw new BusinessException("不能对自己的账号进行操作");
        }
        return Result.whether(service.resetJoinGroup(operator, co.getUsername(), CollectionKit.nullToEmpty(co.getOrganizationCodes())));
    }


    @Override
    public IUserService service() {
        return service;
    }

    @Override
    public String basePermission() {
        return "user:";
    }

}

