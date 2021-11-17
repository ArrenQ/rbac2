package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.EnumSet;
import java.util.Optional;

/**
 * 字段展示的动作
 * @author ath
 */

public enum FieldAction {
    /** 全隐藏 */
    HIDDEN(1),
    /** 半隐藏 */
    HALF_HIDDEN(2);

    @EnumValue
    private final Integer code;

    FieldAction(Integer code) {
        this.code = code;
    }

    public static Optional<FieldAction> valueOf(Integer code) {
        return EnumSet
                .allOf(FieldAction.class)
                .stream()
                .filter(v -> v.code.equals(code))
                .findFirst();
    }
}
