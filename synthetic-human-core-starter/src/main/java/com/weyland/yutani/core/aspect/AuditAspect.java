package com.weyland.yutani.core.aspect;

import com.weyland.yutani.core.annotation.WeylandWatchingYou;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
public class AuditAspect {

    private final AuditSender auditSender;

    public AuditAspect(AuditSender auditSender) {
        this.auditSender = auditSender;
    }

    @Around("@annotation(com.weyland.yutani.core.annotation.WeylandWatchingYou)")
    public Object auditMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();
        String params = Arrays.toString(joinPoint.getArgs());

        String initialAuditMessage = String.format("Executing method '%s' with parameters %s.", methodName, params);
        auditSender.send(initialAuditMessage);

        Object result;
        try {
            result = joinPoint.proceed();
            String successAuditMessage = String.format("Method '%s' executed successfully. Result: %s", methodName, result);
            auditSender.send(successAuditMessage);
            return result;
        } catch (Throwable throwable) {
            String failureAuditMessage = String.format("Method '%s' failed with exception: %s", methodName, throwable.getMessage());
            auditSender.send(failureAuditMessage);
            throw throwable;
        }
    }
}
