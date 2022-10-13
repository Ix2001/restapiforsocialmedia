package com.example.restapiforsocialmedia.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Page not found")
public class PageException extends RuntimeException {
    public PageException(String message) {
        log.error(message);
    }
}