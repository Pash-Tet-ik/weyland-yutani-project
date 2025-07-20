package com.weyland.yutani.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class QueueOverloadException extends RuntimeException {
    public QueueOverloadException(String message) {
        super(message);
    }
}
