package com.chuang.rbac2.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuang.rbac2.crud.entity.I18n;
import com.chuang.rbac2.crud.enums.I18nType;
import com.chuang.rbac2.crud.enums.Language;
import com.chuang.rbac2.crud.event.I18nUpdatedEvent;
import com.chuang.rbac2.crud.mapper.I18nMapper;
import com.chuang.rbac2.crud.service.II18nService;
import com.chuang.tauceti.tools.basic.HashKit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 国际化  服务实现类
 * </p>
 *
 * @author chuang
 * @since 2020-12-21
 */
@Service
public class I18nServiceImpl extends ServiceImpl<I18nMapper, I18n> implements II18nService {

    @Resource private ApplicationContext applicationContext;


    @Override
    public boolean save(I18nType type, String i18n, String message, Language language) {
        boolean success =  findOne(type, i18n, language)
                .map(n -> updateById(n.setMessage(message)))
                .orElseGet(() -> {
                    String md5 = md5(type, i18n, language);
                    I18n o = new I18n().setI18n(i18n).setTypeGroup(type).setLanguage(language).setMessage(message).setMd5(md5).setCreator("admin");
                    return save(o);
                });
        applicationContext.publishEvent(new I18nUpdatedEvent(this, type));
        return success;
    }

    @Override
    public String md5(I18nType type, String i18n, Language language) {
        return HashKit.md5(type + ":" + i18n + ":" + language);
    }

    @Override
    public boolean delete(I18nType type, String i18n) {
        boolean deleted = lambdaUpdate()
                .eq(I18n::getTypeGroup, type)
                .eq(I18n::getI18n, i18n)
                .remove();
        if(deleted) {
            applicationContext.publishEvent(new I18nUpdatedEvent(this, type));
        }
        return deleted;
    }

    @Override
    public boolean delete(I18nType type, String i18n, Language language) {
        boolean deleted = lambdaUpdate()
                .eq(I18n::getTypeGroup, type)
                .eq(I18n::getI18n, i18n)
                .eq(I18n::getLanguage, language)
                .remove();
        if(deleted) {
            applicationContext.publishEvent(new I18nUpdatedEvent(this, type));
        }
        return deleted;
    }


}
