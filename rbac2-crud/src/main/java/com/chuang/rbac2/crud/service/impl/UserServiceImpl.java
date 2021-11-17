package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.Organization;
import com.chuang.rbac2.crud.entity.Position;
import com.chuang.rbac2.crud.entity.User;
import com.chuang.rbac2.crud.enums.RoleType;
import com.chuang.rbac2.crud.mapper.UserMapper;
import com.chuang.rbac2.crud.service.IOrganizationService;
import com.chuang.rbac2.crud.service.IPositionService;
import com.chuang.rbac2.crud.service.IUserService;
import com.chuang.tauceti.tools.basic.collection.CollectionKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户  服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource private IOrganizationService organizationService;
    @Resource private IPositionService positionService;

    @Override
    public boolean assignRole(String username, List<String> roles) {
        baseMapper.deleteAllRole(username);
        baseMapper.assignRole(username, RoleType.USER_ROLE.getCode(), roles);
        return true;
    }

    @Override
    public boolean resetJoinGroup(String operator, String username, List<String> organizationCodes) {
        Set<String> enabledCodeAll = organizationService.enabledAll(operator).stream()
                .filter(Organization::getEnabled)
                .map(Organization::getCode)
                .collect(Collectors.toSet());

        organizationCodes = CollectionKit.intersection(organizationCodes, enabledCodeAll, ArrayList::new);


        List<String> oldPositions = positionService.findByUser(username)
                .stream()
                .map(Position::getPositionCode)
                .collect(Collectors.toList());

        baseMapper.deleteAllOrganization(username);

        if(!organizationCodes.isEmpty()) {
            baseMapper.joinGroup(username, organizationCodes);
        }
        if(oldPositions.isEmpty()) {
            return true;
        } else {
            return resetAppointment(username, oldPositions);
        }
    }

    @Override
    public boolean resetAppointment(String username, List<String> positionCodes) {
        baseMapper.deleteAllPosition(username);
        List<Organization> joined = organizationService.findJoined(username);
        if(joined.isEmpty()) {
            log.warn("{}没有任何组织，却要分配职位", username);
            return false;
        }

        if(positionCodes.isEmpty()) {
            return true;
        }

        List<String> joinedOrgCodes = joined.stream().map(Organization::getCode).collect(Collectors.toList());
        List<Position> validPositions = positionService.findByOrg(joinedOrgCodes);

        List<String> contains = validPositions.stream()
//                .filter(position -> positionCodes.contains(position.getPositionCode()))
//                .map(Position::getPositionCode)
                .map(Position::getPositionCode)
                .filter(positionCodes::contains)
                .collect(Collectors.toList());

        if(contains.isEmpty()) {
            log.warn("给{}分配的职位没有一个正确的", username);
            return false;
        }

        return baseMapper.appointment(username, contains) > 0;
    }
}
