package com.chuang.rbac2.crud.service;

import com.chuang.rbac2.crud.entity.I18n;
import com.chuang.rbac2.crud.enums.I18nType;
import com.chuang.rbac2.crud.enums.Language;
import com.chuang.tauceti.rowquery.IRowQueryService;
import com.chuang.tauceti.tools.basic.HashKit;

import java.util.Optional;

/**
 * <p>
 * 国际化  服务类
 * </p>
 *
 * @author chuang
 * @since 2020-12-21
 */
public interface II18nService extends IRowQueryService<I18n> {

    /**
     * 根据 类型，i18n，语种获取一个 I18n对象
     * @param type 类型
     * @param i18n i18n字符
     * @param language 语言
     * @return I18n对象
     */
    default Optional<I18n> findOne(I18nType type, String i18n, Language language) {
        String md5 = HashKit.md5(type + ":" + i18n + ":" + language);
        return lambdaQuery().eq(I18n::getMd5, md5).eq(I18n::getLanguage, language).oneOpt();
    }

    /**
     * 保存一个I18n记录
     * @param type 类型
     * @param i18n i18n字符串
     * @param message 翻译文本
     * @param language 语言
     * @return 是否存储成功
     */
    boolean save(I18nType type, String i18n, String message, Language language);

    /**
     * 根据传入的参数，计算一个MD5值，用于去重。
     * @param type 类型
     * @param i18n i18n字符串
     * @param language 语言
     * @return MD5唯一值
     */
    String md5(I18nType type, String i18n, Language language);

    /**
     * 删除特定I18n记录（所有语言都删除）
     * @param type 类型
     * @param i18n i18n字符串
     * @return 是否成功删除
     */
    boolean delete(I18nType type, String i18n);

    /**
     * 删除i18n记录中特定语言的记录
     * @param type 类型
     * @param i18n i18n字符串
     * @param language 语言
     * @return 是否成功删除
     */
    boolean delete(I18nType type, String i18n, Language language);



}
