package com.chuang.rbac2.rest.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoggedEvent extends ApplicationEvent {

    private final String username, ip;

    public LoggedEvent(Object source, String username, String ip) {
        super(source);
        this.username = username;
        this.ip = ip;
    }
}
