package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Organization;
import com.chuang.rbac2.crud.entity.Position;
import com.chuang.rbac2.crud.service.IOrganizationService;
import com.chuang.rbac2.crud.service.IPositionService;
import com.chuang.rbac2.rest.model.co.PositionCO;
import com.chuang.rbac2.rest.model.ro.PositionRO;
import com.chuang.rbac2.rest.model.uo.PositionUO;
import com.chuang.tauceti.rowquery.controller.basic.ICrudController;
import com.chuang.tauceti.support.BiValue;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 职位表; 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/position")
public class PositionController implements ICrudController<PositionCO, PositionRO, PositionUO, Position, IPositionService> {

    @Resource private IPositionService service;
    @Resource private IOrganizationService organizationService;

    @Override
    public IPositionService service() {
        return service;
    }

    @RequiresPermissions("user:appointment")
    @GetMapping(value = "/appointed/{username}")
    @ApiOperation("已任命")
    public Result<BiValue<List<PositionRO>, List<PositionRO>>> appointedInfo(@PathVariable @Valid @NotEmpty String username) {

        List<String> orgCodes = organizationService.findJoined(username)
                .stream()
                .map(Organization::getCode)
                .collect(Collectors.toList());
        if(orgCodes.isEmpty()) {
            return Result.success(new BiValue<>(Collections.emptyList(), Collections.emptyList()));
        }

        List<Position> all = service.findByOrg(orgCodes);
        List<Position> appointed = service.findByUser(username);
        return Result.success(new BiValue<>(
                ConvertKit.toBeans(all, PositionRO::new),
                ConvertKit.toBeans(appointed, PositionRO::new)
        ));
    }

    @Override
    public String basePermission() {
        return "position:";
    }

}

