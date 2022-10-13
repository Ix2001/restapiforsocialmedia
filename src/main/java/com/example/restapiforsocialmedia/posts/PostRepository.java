package com.example.restapiforsocialmedia.posts;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor_Username_OrderByDateOfPostDesc (String username, PageRequest pageable);

}
