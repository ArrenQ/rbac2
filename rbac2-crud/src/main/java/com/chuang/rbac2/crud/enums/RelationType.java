package com.chuang.rbac2.crud.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 关系类型
 * @author ath
 * @deprecated
 */

public enum RelationType {
    /** 用户 */
    USER(1),
    /** 组织 */
    ORG(2),
    /** 角色 */
    ROLE(3),
    /** 菜单 */
    MENU(4),

    ;
    @EnumValue
    private final byte code;
    RelationType(int code) {
        this.code = (byte) code;
    }

    public int getCode() {
        return code;
    }
}
