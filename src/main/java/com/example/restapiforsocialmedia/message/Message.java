package com.example.restapiforsocialmedia.message;

import com.example.restapiforsocialmedia.conversation.Conversation;
import com.example.restapiforsocialmedia.user.UserData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 1, max = 4096)
    private String text;
    private LocalDate dateOfDelivery;
    private LocalDate dateOfRead;
    private Boolean isRead;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private UserData author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

}
