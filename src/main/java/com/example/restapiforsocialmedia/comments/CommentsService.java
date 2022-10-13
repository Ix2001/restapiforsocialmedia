package com.example.restapiforsocialmedia.comments;

import com.example.restapiforsocialmedia.exceptions.UserNotFoundException;
import com.example.restapiforsocialmedia.likes.CommentLike;
import com.example.restapiforsocialmedia.likes.CommentsLikeRepository;
import com.example.restapiforsocialmedia.posts.PostRepository;
import com.example.restapiforsocialmedia.user.UserData;
import com.example.restapiforsocialmedia.user.UserDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserDataRepository userDataRepository;
    private final PostRepository postRepository;
    private final CommentsLikeRepository commentsLikeRepository;
    public CommentsService(CommentsRepository commentsRepository, UserDataRepository userDataRepository, PostRepository postRepository, CommentsLikeRepository commentsLikeRepository) {
        this.commentsRepository = commentsRepository;
        this.userDataRepository = userDataRepository;
        this.postRepository = postRepository;
        this.commentsLikeRepository = commentsLikeRepository;
    }


    public void postComment(Long id, Comments comments, String username) {
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        comments.setAuthorOfComment(userData);
        comments.setDateOfComment(LocalDateTime.now());
        comments.setPost(postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post id")));
        comments.setDateOfComment(LocalDateTime.now());
        commentsRepository.save(comments);
    }





    public void deleteComment(Long id, String username) {
        Comments comments = commentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment id"));
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        if (comments.getAuthorOfComment().equals(userData)) {
            commentsRepository.delete(comments);
        }
        else if (comments.getPost().getAuthor().equals(userData)) {
            commentsRepository.delete(comments);
        }
        else {
            throw new IllegalArgumentException("You are not allowed to delete this comment");
        }
    }

    public void updateComment(Long id, Comments comments, String username) {
        Comments comment = commentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment id"));
        UserData userData = userDataRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        if (comment.getAuthorOfComment().equals(userData)) {
            comment.setText(comments.getText());
            commentsRepository.save(comment);
        }
        else {
            throw new IllegalArgumentException("You are not allowed to update this comment");
        }
    }

    public void likeComment(Long id, String name) {
        Comments comments = commentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment id"));
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
        CommentLike commentLike = new CommentLike();
        if (commentsLikeRepository.existsByCommentIdAndLikeAuthor(comments, userData)) {
            commentsLikeRepository.deleteByCommentIdAndLikeAuthor(comments, userData);
        }
        else {
            commentLike.setLikeAuthor(userData);
            commentLike.setCommentId(comments);
            commentsLikeRepository.save(commentLike);
        }
    }
}

