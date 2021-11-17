package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.DictItem;

import java.util.List;

/**
 * <p>
 * 字典项; 服务类
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
public interface IDictItemService extends ITreeService<DictItem> {
    /**
     * 根据字典类型查询字典项
     * @param dictType 字典类型
     * @return 字典项
     */
    default List<DictItem> findByType(String dictType) {
        return lambdaQuery().eq(DictItem::getDictTypeCode, dictType).list();
    }

}
