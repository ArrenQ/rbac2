package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Organization;
import com.chuang.rbac2.crud.entity.Position;
import com.chuang.rbac2.crud.entity.bo.TreeMenuBO;
import com.chuang.rbac2.crud.service.IAuthService;
import com.chuang.rbac2.crud.service.IMenuService;
import com.chuang.rbac2.crud.service.IOrganizationService;
import com.chuang.rbac2.crud.service.IPositionService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.ShiroUser;
import com.chuang.rbac2.rest.model.ro.PositionRO;
import com.chuang.rbac2.rest.model.ro.UserOrgRO;
import com.chuang.rbac2.rest.model.ro.UsersRO;
import com.chuang.tauceti.support.BiValue;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author ath
 */
@Slf4j
@RestController
@RequestMapping("/sys/auth")
public class AuthController {

    @Resource private IMenuService menuService;
    @Resource private IAuthService authService;
    @Resource private IOrganizationService organizationService;
    @Resource private IPositionService positionService;

    @PostMapping("/logout")
    @ApiOperation("登出当前用户")
    public Result<Void> logout() {
        //当前使用的角色(不一定是登录用户)
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success();
    }


    @GetMapping("/users")
    @ApiOperation("获取用户登录后的相关资料")
    public Result<UsersRO> users() {
        ShiroUser shiroUser = OfficeUtils.shiroUserNotNull();
        List<TreeMenuBO> menus = menuService.userMenus(shiroUser.getUsername());
        List<Organization> orgs = organizationService.findJoined(shiroUser.getUsername());

        UsersRO vo = new UsersRO();
        vo.setMenu(menus);
        vo.setUser(shiroUser);
        vo.setUserOrg(ConvertKit.toBeans(orgs, UserOrgRO::new));


        List<String> orgCodes = orgs.stream().map(Organization::getCode).distinct().collect(Collectors.toList());
        Map<String, List<Position>> map = positionService.findByOrg(orgCodes).stream()
                .collect(Collectors.groupingBy(Position::getOrganizationCode));
        vo.getUserOrg().forEach(userOrgRo ->
                userOrgRo.setPositions(ConvertKit.toBeans(map.getOrDefault(userOrgRo.getCode(), Collections.emptyList()), PositionRO::new))
        );

        BiValue<Set<String>, Set<String>> bi = authService.roleAndAbilities(shiroUser.getUsername());
        vo.setRoles(bi.getOne());
        vo.setAbilities(bi.getTwo());

        return Result.success(vo);
    }

}
