package com.chuang.rbac2.rest.controller;


import com.chuang.rbac2.crud.entity.Menu;
import com.chuang.rbac2.crud.service.IMenuService;
import com.chuang.rbac2.crud.service.IUserFastMenuService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.co.MenuCO;
import com.chuang.rbac2.rest.model.ro.MenuRO;
import com.chuang.rbac2.rest.model.uo.MenuUO;
import com.chuang.rbac2.rest.model.uo.TreeMoveUO;
import com.chuang.tauceti.rowquery.controller.basic.ICrudController;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表; 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2020-12-20
 */
@Valid
@Api(tags = "菜单模块")
@RestController
@RequestMapping("/sys/menu")
public class MenuController implements ICrudController<MenuCO, MenuRO, MenuUO, Menu, IMenuService> {

    @Resource private IMenuService menuService;
    @Resource private IUserFastMenuService userFastMenuService;

    @PostMapping("/move")
    @ApiOperation("移动菜单")
    public Result<Boolean> move(@RequestBody @ApiParam TreeMoveUO move) {
        return Result.success(this.menuService.move(move.getFrom(), move.getTo(), move.getPos()));
    }

    @GetMapping("/all")
    public Result<List<MenuRO>> menu() {
        return Result.success(
                menuService.list().stream().map(menu -> ConvertKit.toBean(menu, MenuRO::new))
                .sorted(Comparator.comparing(MenuRO::getSortRank))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/fast/all")
    public Result<Set<String>> fastAll() {
        return Result.success(
                userFastMenuService.findByUsername(OfficeUtils.shiroUserNotNull().getUsername())
        );
    }

    @PostMapping("/fast/setting")
    @ApiOperation("设置快捷菜单")
    public Result<Boolean> settingFast(@Valid @RequestBody @NotEmpty List<String> fast) {
        return Result.success(userFastMenuService.fast(OfficeUtils.shiroUserNotNull().getUsername(), fast));
    }

    @Override
    public String basePermission() {
        return "menu:";
    }

    @Override
    public IMenuService service() {
        return menuService;
    }
}

