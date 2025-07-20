package com.weyland.yutani.core.aspect.impl;

import com.weyland.yutani.core.aspect.AuditSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleAuditSender implements AuditSender {

    private static final Logger log = LoggerFactory.getLogger(ConsoleAuditSender.class);

    @Override
    public void send(String auditData) {
        log.info("[WEYLAND-YUTANI AUDIT]: {}", auditData);
    }
}
