package com.chuang.rbac2.crud.enums;

/**
 * ACL 验证类型
 * @author ath
 */

public enum ACLMode {
    /** ACL中列出的所有权限，必须全部满足才为 真 */
    allOf,
    /** ACL中列出的所有权限，满足一个即为 真 */
    oneOf
}
