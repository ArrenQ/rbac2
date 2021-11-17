package com.chuang.rbac2.crud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuang.rbac2.crud.entity.Organization;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 组织表; Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    @Select("SELECT * FROM sys_organization o " +
            "LEFT JOIN sys_user_organization uo " +
            "ON o.id = uo.organization_id " +
            "WHERE uo.username=#{username} ")
    List<Organization> findJoined(String username);
}
