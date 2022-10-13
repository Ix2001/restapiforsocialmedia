package com.example.restapiforsocialmedia.likes;

import com.example.restapiforsocialmedia.comments.Comments;
import com.example.restapiforsocialmedia.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByCommentIdAndLikeAuthor(Comments commentId, UserData likeAuthor);

    void deleteByCommentIdAndLikeAuthor(Comments comments, UserData userData);
}

