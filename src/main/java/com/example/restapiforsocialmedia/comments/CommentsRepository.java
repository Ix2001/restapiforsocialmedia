package com.example.restapiforsocialmedia.comments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
