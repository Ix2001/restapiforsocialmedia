package com.example.restapiforsocialmedia.followers;

import com.example.restapiforsocialmedia.user.UserData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowersRepository extends JpaRepository<Followers, Long> {
    List<Followers> findByFrom(UserData from, PageRequest pageRequest);
    List<Followers> findByFrom(UserData from);
    List<Followers> findByTo(UserData to, PageRequest pageRequest);
    List<Followers> findByTo(UserData to);

    Followers findByFromAndTo(UserData from, UserData to);

}
