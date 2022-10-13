package com.example.restapiforsocialmedia.posts;

import com.example.restapiforsocialmedia.comments.CommentsPublicDTO;
import com.example.restapiforsocialmedia.content.MediaContent;
import com.example.restapiforsocialmedia.user.UserDataBasicPublicDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostPublicDTO {
    private String text;
    private List<MediaContent> postPhotos;
    private LocalDateTime dateOfPost;
    private UserDataBasicPublicDTO author;
    private List<CommentsPublicDTO> comments;
    private Integer likesCount;
}

