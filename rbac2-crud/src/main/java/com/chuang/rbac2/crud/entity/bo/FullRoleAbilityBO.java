package com.chuang.rbac2.crud.entity.bo;

import com.chuang.rbac2.crud.entity.LicensableAbility;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 完整的角色权限信息
 * @author ath
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FullRoleAbilityBO extends LicensableAbility {

    /** 当前用户可操作这个权限(可将此权限赋权给某个角色) */
    private Boolean disableOpt;

    /** 当前角色是否已经被选择（赋权）过这个权限 */
    private Boolean checked;


}
