package com.example.restapiforsocialmedia.comments;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentsController {
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/{id}")
    public void postComment(@PathVariable Long id, @RequestBody Comments comments, Authentication authentication) {
        commentsService.postComment(id, comments, authentication.getName());
    }
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, Authentication authentication) {
        commentsService.deleteComment(id, authentication.getName());
    }
    @PatchMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody Comments comments, Authentication authentication) {
        commentsService.updateComment(id, comments, authentication.getName());
    }
    @PatchMapping("/like/{id}")
    public void likeComment(@PathVariable Long id, Authentication authentication) {
        commentsService.likeComment(id, authentication.getName());
    }
}

