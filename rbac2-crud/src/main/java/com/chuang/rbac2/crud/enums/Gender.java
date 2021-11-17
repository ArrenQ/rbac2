package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 性别类型
 * @author ath
 */

public enum Gender {
    /** 男 */
    MALE(0),
    /** 女 */
    FEMALE(1),
    /** 新人类 */
    LADY_BOY(2);

    @EnumValue
    private final byte code;

    Gender(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return this.code;
    }
}
