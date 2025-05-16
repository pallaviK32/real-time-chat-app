package com.pallavi.messaging_service.service;

import com.pallavi.messaging_service.model.ChatMessage;
import com.pallavi.messaging_service.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage)
    {
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }
}
