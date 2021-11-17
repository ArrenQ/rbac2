package com.chuang.rbac2.crud.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuang.rbac2.crud.entity.I18n;

/**
 * <p>
 * 国际化  Mapper 接口
 * </p>
 *
 * @author chuang
 * @since 2020-12-21
 */
@InterceptorIgnore(illegalSql = "true")
public interface I18nMapper extends BaseMapper<I18n> {

}
