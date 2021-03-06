package com.chuang.rbac2.crud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuang.rbac2.crud.entity.Position;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 职位表; Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2021-05-24
 */
public interface PositionMapper extends BaseMapper<Position> {

    @Select("select p.* from sys_position p " +
            "left join sys_user_position up " +
            "on p.id = up.position_id " +
            "where up.username=#{username} ")
    List<Position> findByUser(String username);
}
