package com.example.restapiforsocialmedia.admin;

import com.example.restapiforsocialmedia.posts.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/post")
public class AdminPostController {
    private final PostService postService;

    public AdminPostController(PostService postService) {
        this.postService = postService;
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
    @PatchMapping("/edit/{id}")
    public void editPost(@PathVariable Long id, @RequestBody String text) {
        postService.editPost(id, text);
    }

    @GetMapping("/statistics/most-popular/{page}")
    public void getMostPopular(@PathVariable int page) {
        postService.getMostPopular(page);
    }
}
