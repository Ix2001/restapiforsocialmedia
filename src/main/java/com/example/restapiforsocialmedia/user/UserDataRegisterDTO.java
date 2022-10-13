package com.example.restapiforsocialmedia.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDataRegisterDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private LocalDate birthday;
    private String userPhoto;

}