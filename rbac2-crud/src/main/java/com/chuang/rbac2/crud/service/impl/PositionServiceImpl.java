package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.Position;
import com.chuang.rbac2.crud.mapper.PositionMapper;
import com.chuang.rbac2.crud.service.IPositionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 职位表; 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

    @Override
    public List<Position> findByUser(String username) {
        return baseMapper.findByUser(username);
    }
}
