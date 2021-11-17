package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.EnumSet;
import java.util.Optional;

/**
 * 权限类型
 * @author ath
 */

public enum AbilityType {
    /** 菜单展示 */
    MENU(1),
    /** 功能展示 */
    FUNCTION(2),
    /** 字段展示 */
    FIELD(3);

    @EnumValue
    private final Integer code;

    AbilityType(Integer code) {
        this.code = code;
    }

    public static Optional<AbilityType> valueOf(Integer code) {
        return EnumSet
                .allOf(AbilityType.class)
                .stream()
                .filter(v -> v.code.equals(code))
                .findFirst();
    }
}
