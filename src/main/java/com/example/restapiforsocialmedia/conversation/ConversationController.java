package com.example.restapiforsocialmedia.conversation;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public List<Conversation> getConversations(Authentication authentication) {
        return conversationService.getConversations(authentication.getName());
    }
    @PostMapping
    public void createConversation(Authentication authentication, @RequestBody ConversationRegisterDTO conversationRegisterDTO) {
        conversationService.createConversation(authentication.getName(), conversationRegisterDTO);
    }
}

