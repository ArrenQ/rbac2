package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 角色类型
 * @author ath
 */

public enum  RoleType {
    /** 用户角色，用户自身的角色 */
    USER_ROLE(0),
    /** 组织角色，当用户加入某个组织后，用户会享有这个组织的角色 */
    ORG_ROLE(1),
    /** 职位角色，当用户在某个组织当然某个职位后，用户会享有这个职位的角色 */
    POS_ROLE(2);

    @EnumValue
    private final byte code;

    RoleType(int code) {
        this.code = (byte) code;
    }

    public int getCode() {
        return code;
    }
}
