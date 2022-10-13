package com.example.restapiforsocialmedia.comments;

import com.example.restapiforsocialmedia.user.UserDataBasicPublicDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentsPublicDTO {
    private Long id;
    private String text;
    private LocalDateTime dateOfComment;
    private UserDataBasicPublicDTO authorOfComment;
}
