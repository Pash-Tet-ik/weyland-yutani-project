package com.weyland.yutani.core.aspect;

public interface AuditSender {

    void send(String auditData);
}
