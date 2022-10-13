package com.example.restapiforsocialmedia.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class UserAuthException extends UsernameNotFoundException {
    public UserAuthException(String message) {
        super(message);
    }
}
