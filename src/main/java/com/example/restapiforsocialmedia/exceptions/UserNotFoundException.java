package com.example.restapiforsocialmedia.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        log.error("User not found: " + username);
    }
}
