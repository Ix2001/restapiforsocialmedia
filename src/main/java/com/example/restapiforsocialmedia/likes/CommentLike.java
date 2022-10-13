package com.example.restapiforsocialmedia.likes;

import com.example.restapiforsocialmedia.comments.Comments;
import com.example.restapiforsocialmedia.user.UserData;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "comment_like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private Comments commentId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_of_like")
    private UserData likeAuthor;
}
