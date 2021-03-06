package com.chuang.rbac2.rest.model.uo;

import com.chuang.rbac2.crud.enums.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 角色表;角色
 * </p>
 *
 * @author chuang
 * @since 2021-05-07
 */
@Data
@Accessors(chain = true)
@ApiModel(value="RoleUO对象", description="角色表;角色")
public class RoleUO implements Serializable {

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "角色标识")
    @NotBlank(message = "角色标识不能为空")
    private String role;

    @ApiModelProperty(value = "角色类型")
    @NotNull(message = "角色类型不能为空")
    private RoleType roleType;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "简介")
    @NotBlank(message = "简介不能为空")
    private String description;

    @ApiModelProperty(value = "是否启用")
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

}
