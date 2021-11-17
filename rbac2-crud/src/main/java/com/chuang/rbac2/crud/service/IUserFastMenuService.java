package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.UserFastMenu;
import com.chuang.tauceti.rowquery.IRowQueryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户快捷菜单  服务类
 * </p>
 *
 * @author chuang
 * @since 2020-12-20
 */
public interface IUserFastMenuService extends IRowQueryService<UserFastMenu> {

    /**
     * 通过用户名查询快捷菜单
     * @param username 用户名
     * @return 快捷菜单编号集合
     */
    default Set<String> findByUsername(String username) {
        return lambdaQuery().eq(UserFastMenu::getUsername, username).list()
                .stream()
                .map(UserFastMenu::getMenuCode)
                .collect(Collectors.toSet());
    }

    /**
     * 给用户重置快捷菜单，此方法会先删除用户所有快捷菜单，然后再添加新的快捷菜单集合。
     * @param username 用户名
     * @param fast 快捷菜单集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean fast(String username, List<String> fast) {
        lambdaUpdate().eq(UserFastMenu::getUsername, username).remove();

        Set<UserFastMenu> fastMenus = fast.stream().distinct()
                .map(s -> new UserFastMenu().setMenuCode(s).setUsername(username))
                .collect(Collectors.toSet());
        return saveBatch(fastMenus);
    }
}
