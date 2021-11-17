package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.Api;
import com.chuang.tauceti.rowquery.IRowQueryService;
import com.chuang.tauceti.tools.basic.FileKit;
import com.chuang.tauceti.tools.basic.HashKit;

import java.util.List;

/**
 * <p>
 * 接口  服务类
 * </p>
 *
 * @author chuang
 * @since 2021-04-30
 */
public interface IApiService extends IRowQueryService<Api> {
    /**
     * 查找所有启用的Api配置
     * @return Api配置
     */
    default List<Api> findEnablers() {
        return lambdaQuery()
                .eq(Api::getEnabled, true).list();
    }

    /**
     * 生成一个唯一编号
     * @param url 接口url
     * @param method 接口 method
     * @return md5唯一编号
     */
    default String genCode(String url, String method) {
        if (url.endsWith(FileKit.UNIX_SEPARATOR + "") || url.endsWith(FileKit.WINDOWS_SEPARATOR + "")) {
            return genCode(url.substring(0, url.length() - 1), method);
        } else {
            return HashKit.md5(method.toUpperCase() + ":" + url);
        }
    }

}
