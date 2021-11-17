package com.chuang.rbac2.crud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ATH
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LicensableAbility extends Ability {

    @TableField(value = "licensable")
    private Boolean licensable;
}
