package com.example.restapiforsocialmedia.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(value = org.springframework.http.HttpStatus.CONFLICT, reason = "User already exists")
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        log.error("User with username {} already exists", username);
    }
}
