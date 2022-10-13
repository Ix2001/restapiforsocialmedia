package com.example.restapiforsocialmedia.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDataLoginDTO {
    private String username;
    private String password;
}
