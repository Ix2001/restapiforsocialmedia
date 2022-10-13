package com.example.restapiforsocialmedia.conversation;

import lombok.Data;

import java.util.List;

@Data
public class ConversationRegisterDTO {
    private Long id;
    private String nameOfConversation;
    private List<String> participants;
}