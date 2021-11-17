package com.chuang.rbac2.rest.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ModuleInitParams {

    @NotEmpty
    private String name;
    @NotEmpty
    private String code;
    @NotEmpty
    private String parentMenu;
    @NotEmpty
    private String parentAbility;
    @NotEmpty
    private String abilityPrefix;
    @NotEmpty
    private String routePath;
}
