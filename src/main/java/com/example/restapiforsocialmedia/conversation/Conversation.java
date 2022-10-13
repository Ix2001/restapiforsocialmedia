package com.example.restapiforsocialmedia.conversation;

import com.example.restapiforsocialmedia.message.Message;
import com.example.restapiforsocialmedia.user.UserData;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateOfConversation;
    private String nameOfConversation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "conversation_person",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<UserData> participants;

    // list of messages
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messages;
}
