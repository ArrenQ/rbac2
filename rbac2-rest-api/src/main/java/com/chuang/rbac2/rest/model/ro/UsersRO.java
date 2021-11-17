package com.chuang.rbac2.rest.model.ro;

import com.chuang.rbac2.crud.entity.bo.TreeMenuBO;
import com.chuang.rbac2.rest.model.ShiroUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@ApiModel(description = "用户相关信息")
public class UsersRO {
    @ApiModelProperty("用户")
    private ShiroUser user;
    @ApiModelProperty("所在部门")
    private List<UserOrgRO> userOrg;
    @ApiModelProperty("菜单")
    private List<TreeMenuBO> menu;
    @ApiModelProperty("供客户端ACL用的角色点")
    private Collection<String> roles;
    @ApiModelProperty("供客户端ACL用的权限点")
    private Collection<String> abilities;

}
