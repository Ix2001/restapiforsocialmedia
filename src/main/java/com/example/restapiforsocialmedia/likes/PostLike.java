package com.example.restapiforsocialmedia.likes;

import com.example.restapiforsocialmedia.posts.Post;
import com.example.restapiforsocialmedia.user.UserData;
import javax.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post postId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_of_like")
    private UserData authorOfLike;
}
