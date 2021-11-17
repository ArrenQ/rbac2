package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.Role;
import com.chuang.rbac2.crud.mapper.RoleMapper;
import com.chuang.rbac2.crud.service.IRoleService;
import com.chuang.tauceti.tools.basic.collection.CollectionKit;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表;角色 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Override
    public Collection<Role> findAllRoles(String username) {

        List<Role> roles = baseMapper.findUserRoles(username);

        List<Role> rs = baseMapper.findOrgRole(username);
        if(CollectionKit.isNotEmpty(rs)) {
            roles.addAll(rs);
        }
        rs = baseMapper.findPositionRole(username);
        if(CollectionKit.isNotEmpty(rs)) {
            roles.addAll(rs);
        }


        return CollectionKit.distinct(roles, Role::getId);
    }

    @Override
    public List<Role> findUserRoles(String username) {
        return baseMapper.findUserRoles(username);
    }
}
