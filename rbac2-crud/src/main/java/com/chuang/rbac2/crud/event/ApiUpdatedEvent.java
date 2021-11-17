package com.chuang.rbac2.crud.event;

import org.springframework.context.ApplicationEvent;

/**
 * Api配置更新事件
 * @author ath
 */
public class ApiUpdatedEvent extends ApplicationEvent {

    public ApiUpdatedEvent(Object source) {
        super(source);
    }
}
