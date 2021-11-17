package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Position;
import com.chuang.tauceti.rowquery.IRowQueryService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 职位表; 服务类
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
public interface IPositionService extends IRowQueryService<Position> {

    /**
     * 根据用户名查询用户所担任的全部职位
     * @param username 用户名
     * @return 职位
     */
    List<Position> findByUser(String username);

    /**
     * 根据组织，查询所有职位
     * @param org 组织编号数组
     * @return 职位
     */
    default List<Position> findByOrg(String... org) {
        return findByOrg(Arrays.asList(org));
    }

    /**
     * 根据组织，查询所有职位
     * @param orgCodes 组织编号集合
     * @return 职位
     */
    default List<Position> findByOrg(List<String> orgCodes) {
        if(orgCodes.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(Position::getOrganizationCode, orgCodes)
                .list();
    }
}
