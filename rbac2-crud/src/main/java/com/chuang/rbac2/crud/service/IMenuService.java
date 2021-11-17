package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Menu;
import com.chuang.rbac2.crud.entity.bo.TreeMenuBO;
import com.chuang.tauceti.rowquery.IRowQueryService;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 菜单表; 服务类
 * </p>
 *
 * @author chuang
 * @since 2020-12-20
 */
public interface IMenuService extends IRowQueryService<Menu>, ITreeService<Menu> {

    /**
     * 用户菜单
     * @param username 用户名
     * @return 树结构菜单
     */
    List<TreeMenuBO> userMenus(String username);

    default Optional<Menu> findByCoe(String code) {
        return lambdaQuery().eq(Menu::getCode, code).oneOpt();
    }
}
