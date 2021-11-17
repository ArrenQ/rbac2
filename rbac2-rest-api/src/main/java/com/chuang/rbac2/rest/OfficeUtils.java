package com.chuang.rbac2.rest;

import com.chuang.rbac2.rest.model.ShiroUser;
import com.chuang.tauceti.shiro.spring.web.jwt.ShiroUtils;

import java.util.Optional;

/**
 * @author ATH
 */
public class OfficeUtils extends ShiroUtils {

    public static Optional<ShiroUser> shiroUser() {
        return loggedUser();
    }

    public static ShiroUser shiroUserNotNull() {
        return loggedUserNotNull();
    }


}
