package com.chuang.rbac2.rest.model.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class LicensableAbilityRO extends AbilityRO {

    @ApiModelProperty("是否可向下授权")
    private Boolean licensable;
}
