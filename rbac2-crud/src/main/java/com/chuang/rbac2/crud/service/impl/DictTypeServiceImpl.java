package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.DictType;
import com.chuang.rbac2.crud.mapper.DictTypeMapper;
import com.chuang.rbac2.crud.service.IDictTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型; 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

}
