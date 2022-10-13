package com.example.restapiforsocialmedia.conversation;

import com.example.restapiforsocialmedia.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("select c from Conversation c where :person member of c.participants")
    List<Conversation> findAllByParticipant(@Param("person") UserData userData);

}
