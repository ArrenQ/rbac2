package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.Menu;
import com.chuang.rbac2.crud.entity.bo.AclBO;
import com.chuang.rbac2.crud.entity.bo.TreeMenuBO;
import com.chuang.rbac2.crud.mapper.MenuMapper;
import com.chuang.rbac2.crud.service.IMenuService;
import com.chuang.rbac2.crud.service.IUserFastMenuService;
import com.chuang.tauceti.support.BiValue;
import com.chuang.tauceti.tools.basic.StringKit;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import com.chuang.tauceti.tools.basic.tree.Node;
import com.chuang.tauceti.tools.basic.tree.NodeBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表; 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2020-12-20
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource private IUserFastMenuService userFastMenuService;

    @Override
    public List<TreeMenuBO> userMenus(String username) {
        List<Menu> list = lambdaQuery().eq(Menu::getEnabled, true).list();
        Set<String> fastMenus = userFastMenuService.findByUsername(username);
        return convert(list, fastMenus);
    }


    private List<TreeMenuBO> convert(Collection<Menu> menus, Set<String> fastMenus) {
        List<Node<Menu>> tree = new NodeBuilder<Integer, Menu>().relation(menu -> {
            Integer pid = menu.getParentId();
            if(null == pid || pid == 0 || pid.equals(menu.getId())) {
                pid = null;
            }
            return new BiValue<>(menu.getId(), pid);
        }).toNode(menus);

        return convert(tree, fastMenus);
    }

    private List<TreeMenuBO> convert(List<Node<Menu>> menus, Set<String> fastMenus) {
        return menus.stream()
                .sorted(Comparator.comparingInt(o -> o.getSource().getSortRank()))
                .map(node -> {
                    Menu m = node.getSource();
                    TreeMenuBO bo = ConvertKit.toBean(m, TreeMenuBO::new);
                    bo.setDisabled(!m.getEnabled());
                    bo.setShortcut(fastMenus.contains(m.getCode()));
                    bo.setSort(m.getSortRank());
                    bo.setKey(m.getId() + "");
                    bo.setChildren(convert(node.getChildren(), fastMenus));
                    if (StringKit.isNotBlank(m.getAcl())) {
                        bo.setAcl(AclBO.parse(m.getAcl()));
                    }
                    return bo;
                })
                .collect(Collectors.toList());
    }
}
