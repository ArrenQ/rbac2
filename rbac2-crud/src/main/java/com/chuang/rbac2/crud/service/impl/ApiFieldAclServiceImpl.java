package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.ApiFieldAcl;
import com.chuang.rbac2.crud.mapper.ApiFieldAclMapper;
import com.chuang.rbac2.crud.service.IApiFieldAclService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * api字段权限  服务实现类
 * </p>
 *
 * @author chuang
 * @since 2021-05-01
 */
@Service
public class ApiFieldAclServiceImpl extends ServiceImpl<ApiFieldAclMapper, ApiFieldAcl> implements IApiFieldAclService {

}
