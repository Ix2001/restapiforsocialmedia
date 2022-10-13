package com.example.restapiforsocialmedia.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUsername(String username);

    List<UserData> findByUsernameContaining(String username, PageRequest pageable);

    List<UserData> findByOrderByFollowersDesc(PageRequest pageable);
}

