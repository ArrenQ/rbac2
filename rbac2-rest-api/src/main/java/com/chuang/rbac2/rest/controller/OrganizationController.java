package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Organization;
import com.chuang.rbac2.crud.service.IOrganizationService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.co.OrganizationCO;
import com.chuang.rbac2.rest.model.ro.OrganizationRO;
import com.chuang.rbac2.rest.model.uo.OrganizationUO;
import com.chuang.rbac2.rest.model.uo.TreeMoveUO;
import com.chuang.tauceti.rowquery.controller.basic.ICrudController;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 组织表; 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/sys/organization")
public class OrganizationController implements ICrudController<OrganizationCO, OrganizationRO, OrganizationUO, Organization, IOrganizationService> {

    @Resource private IOrganizationService service;

    @PostMapping("/move")
    @ApiOperation("移动菜单")
    public Result<Boolean> move(@RequestBody @ApiParam TreeMoveUO move) {
        return Result.success(this.service.move(move.getFrom(), move.getTo(), move.getPos()));
    }

    @GetMapping("/all")
    public Result<List<OrganizationRO>> all() {
        return Result.success(
                ConvertKit.toBeans(this.service.enabledAll(OfficeUtils.shiroUserNotNull().getUsername()), OrganizationRO::new)
        );
    }

    @RequiresPermissions("user:join-group")
    @GetMapping(value = "/joined/{username}")
    @ApiOperation("已经加入组织")
    public Result<List<OrganizationRO>> joinedOrg(@PathVariable @Valid @NotEmpty String username) {
        List<Organization> list = service.findJoined(username);
        return Result.success(ConvertKit.toBeans(list, OrganizationRO::new));
    }

    @Override
    public IOrganizationService service() {
        return service;
    }

    @Override
    public String basePermission() {
        return "organization:";
    }

}

