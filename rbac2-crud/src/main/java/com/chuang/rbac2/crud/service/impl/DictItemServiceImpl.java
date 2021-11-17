package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.DictItem;
import com.chuang.rbac2.crud.mapper.DictItemMapper;
import com.chuang.rbac2.crud.service.IDictItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典项; 服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements IDictItemService {

}
