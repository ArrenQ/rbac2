package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.Config;
import com.chuang.rbac2.crud.mapper.ConfigMapper;
import com.chuang.rbac2.crud.service.IConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表  服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
