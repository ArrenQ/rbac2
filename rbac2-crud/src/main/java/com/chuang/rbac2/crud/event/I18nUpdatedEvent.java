package com.chuang.rbac2.crud.event;

import com.chuang.rbac2.crud.enums.I18nType;
import org.springframework.context.ApplicationEvent;

/**
 * 国际化更新事件
 * @author ath
 */
public class I18nUpdatedEvent extends ApplicationEvent {

    private final I18nType type;

    public I18nUpdatedEvent(Object source, I18nType type) {
        super(source);
        this.type = type;
    }

    public I18nType getType() {
        return type;
    }
}
