package com.chuang.rbac2.rest.model.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TreeRO {
    @ApiModelProperty("节点ID")
    private Integer id;
    @ApiModelProperty("父节点ID")
    private Integer parentId;
}
