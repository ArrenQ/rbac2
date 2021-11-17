package com.chuang.rbac2.rest.model.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserOrgRO extends OrganizationRO {
    @ApiModelProperty("职位")
    private List<PositionRO> positions;
}
