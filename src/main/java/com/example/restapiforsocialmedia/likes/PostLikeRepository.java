package com.example.restapiforsocialmedia.likes;

import com.example.restapiforsocialmedia.posts.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // find all likes by post id
    List<PostLike> findAllByPostId(Post postId);
    List<PostLike> findAllByPostId(Post postId, PageRequest pageable);
}
