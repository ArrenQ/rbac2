package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Organization;
import com.chuang.tauceti.rowquery.IRowQueryService;
import com.chuang.tauceti.tools.basic.StringKit;
import com.chuang.tauceti.tools.basic.collection.CollectionKit;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织表; 服务类
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
public interface IOrganizationService extends IRowQueryService<Organization>, com.chuang.rbac2.crud.service.ITreeService<Organization> {

    /**
     * 查找用户加入的所有组织。
     * @param username 用户名
     * @return 用户加入的所有组织
     */
    List<Organization> findJoined(String username);

    /**
     * 根据传入的用户名获取所有组织信息，并对组织的enabled进行处理。
     * @param username 操作用户 通常是当前登录用户的用户名
     * @return 所有组织
     */
    default List<Organization> enabledAll(String username) {
        Set<Integer> joined = findJoined(username)
                .stream().map(Organization::getId).collect(Collectors.toSet());

        List<Organization> list = list();
        list.forEach(organization -> {
             /*
             所有当前用户没有加入的组织都把enabled设为只false，已经是false的跳过。
             当前用户只能操作自己加入的部门
             */
            if(!organization.getEnabled()) {
                return;
            }
            Set<Integer> parentIds = Arrays.stream(organization.getParents().split("/"))
                    .filter(StringKit::isNotBlank)
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            parentIds.add(organization.getId());
            organization.setEnabled(CollectionKit.containsAny(parentIds, joined));
        });
       return list;
    }
}
