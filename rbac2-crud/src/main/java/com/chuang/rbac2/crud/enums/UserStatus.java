package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 用户状态
 * @author ath
 */

public enum UserStatus {
    /** 正常 */
    NORMAL(0),
    /** 锁定 */
    LOCKED(1);

    @EnumValue
    private final byte code;

    UserStatus(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return this.code;
    }
}
