package com.example.restapiforsocialmedia.message;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageServise messageServise;

    public MessageController(MessageServise messageServise) {
        this.messageServise = messageServise;
    }

    @PostMapping("/send/{conversationId}")
    public Message send(@PathVariable Long conversationId, @RequestBody Message message, Authentication authentication) {
        return messageServise.send(conversationId, message, authentication);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteMyMessage(Authentication authentication, @PathVariable Long id) {
        messageServise.deleteMyMessage(authentication.getName(), id);
    }
    @PatchMapping("/edit/{id}")
    public void editMyMessage(Authentication authentication, @PathVariable Long id, @RequestBody Message message) {
        messageServise.editMyMessage(authentication.getName(), id, message);
    }

}
