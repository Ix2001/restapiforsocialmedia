package com.example.restapiforsocialmedia.user;

import lombok.Data;

@Data
public class UserDataBasicPublicDTO {
    private String username;
    private String name;
    private String surname;
    private String profilePicture;

    public UserDataBasicPublicDTO(String username, String name, String surname) {
    }

    public UserDataBasicPublicDTO() {

    }
}
