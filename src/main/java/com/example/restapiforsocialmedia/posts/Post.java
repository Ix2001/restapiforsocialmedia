package com.example.restapiforsocialmedia.posts;

import com.example.restapiforsocialmedia.comments.Comments;
import com.example.restapiforsocialmedia.content.MediaContent;
import com.example.restapiforsocialmedia.user.UserData;
import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 1, max = 2048)
    private String text;
    @Column(name = "date_of_post")
    private LocalDateTime dateOfPost;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private UserData author;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comments> comments;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MediaContent> mediaContents;
}
