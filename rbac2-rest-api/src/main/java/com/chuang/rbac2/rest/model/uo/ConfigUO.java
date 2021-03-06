package com.chuang.rbac2.rest.model.uo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 系统配置表 
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@Data
@Accessors(chain = true)
@ApiModel(value="ConfigUO对象", description="系统配置表 ")
public class ConfigUO implements Serializable {
    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "编码")
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty(value = "值")
    @NotBlank(message = "值不能为空")
    private String value;


    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "值校验")
    @NotBlank(message = "值校验不能为空")
    private String valueRegex;

}
