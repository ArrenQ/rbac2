package com.chuang.rbac2.rest.model.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色对应的所有权限
 * @author ath
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FullRoleAbilityRO extends LicensableAbilityRO {
    @ApiModelProperty("当前登录的用户是否可对其进行操作")
    private Boolean disableOpt;
    @ApiModelProperty("是否已选择")
    private Boolean checked;
}
