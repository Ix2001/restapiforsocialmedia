package com.example.restapiforsocialmedia.message;

import com.example.restapiforsocialmedia.conversation.ConversationRepository;
import com.example.restapiforsocialmedia.exceptions.UserNotFoundException;
import com.example.restapiforsocialmedia.user.UserData;
import com.example.restapiforsocialmedia.user.UserDataRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MessageServise {
    private final MessageRepository messageRepository;
    private final UserDataRepository userDataRepository;
    private final ConversationRepository conversationRepository;

    public MessageServise(MessageRepository messageRepository, UserDataRepository userDataRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.userDataRepository = userDataRepository;
        this.conversationRepository = conversationRepository;
    }




    public void deleteMyMessage(String name, Long id) {
        if (messageRepository.findById(id).get().getAuthor().getUsername().equals(name)) {
            messageRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("You can't delete this message");
        }
    }

    public void editMyMessage(String name, Long id, Message message) {
        if (messageRepository.findById(id).get().getAuthor().getUsername().equals(name)) {
            messageRepository.findById(id).get().setText(message.getText());
            messageRepository.save(messageRepository.findById(id).get());
        }
        else {
            throw new IllegalArgumentException("You can't edit this message");
        }
    }

    public Message send(Long conversationId, Message message, Authentication authentication) {
        UserData author = userDataRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
        message.setAuthor(author);
        message.setConversation(conversationRepository.findById(conversationId).get());
        message.setDateOfDelivery(LocalDate.now());
        message.setIsRead(false);
        return messageRepository.save(message);
    }
}

