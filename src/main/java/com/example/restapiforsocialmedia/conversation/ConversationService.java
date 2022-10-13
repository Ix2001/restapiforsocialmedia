package com.example.restapiforsocialmedia.conversation;

import com.example.restapiforsocialmedia.exceptions.UserNotFoundException;
import com.example.restapiforsocialmedia.user.UserData;
import com.example.restapiforsocialmedia.user.UserDataBasicPublicDTO;
import com.example.restapiforsocialmedia.user.UserDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserDataRepository userDataRepository;

    public ConversationService(ConversationRepository conversationRepository, UserDataRepository userDataRepository) {
        this.conversationRepository = conversationRepository;
        this.userDataRepository = userDataRepository;
    }

    public List<Conversation> getConversations(String name) {
        UserData userData = userDataRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Conversation> conversations = conversationRepository.findAllByParticipant(userData);
        for (Conversation conversation : conversations) {
            List<UserData> participants = conversation.getParticipants();
            List<UserDataBasicPublicDTO> userDataBasicPublicDTOS = new ArrayList<>();
            for (UserData participant : participants) {
                userDataBasicPublicDTOS.add(new UserDataBasicPublicDTO(participant.getUsername(), participant.getName(), participant.getSurname()));
            }
        }
        return conversations;
    }

    public void createConversation(String mySelf, ConversationRegisterDTO conversationRegisterDTO) {
        Conversation conversation = new Conversation();
        List<UserData> members = new ArrayList<>();
        conversationRegisterDTO.getParticipants().add(mySelf);
        conversationRegisterDTO.getParticipants().forEach(participant -> {
            UserData userData = userDataRepository.findByUsername(participant).orElseThrow(() -> new UserNotFoundException(participant));
            members.add(userData);
        });
        conversation.setParticipants(members);
        conversation.setDateOfConversation(LocalDateTime.now());
        for (UserData member : members) {
            log.info(member.getUsername());
        }
        if (conversationRegisterDTO.getParticipants().size() == 2) {
            conversation.setNameOfConversation(null);
        } else {
            if (conversationRegisterDTO.getNameOfConversation() == null) {
                conversation.setNameOfConversation("New conversation");
            } else {
                conversation.setNameOfConversation(conversationRegisterDTO.getNameOfConversation());
            }
        }
        conversationRepository.save(conversation);

    }
}
