package com.example.restapiforsocialmedia.user;

import com.example.restapiforsocialmedia.content.MediaContent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDataPublicDTO {
    private Long id;
    private String username;
    private List<MediaContent> profilePictures;
    private String name;
    private String surname;
    private String email;
    private LocalDate birthday;
    private Integer followersCount;
    private Integer followingCount;
}
