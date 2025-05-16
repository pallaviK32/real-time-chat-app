package com.pallavi.messaging_service.controller;

//import com.pallavi.messaging_service.dto.Message;
import com.pallavi.messaging_service.model.ChatMessage;
import com.pallavi.messaging_service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


@MessageMapping("/public")
@SendTo("/topic/messages")
public ChatMessage sendPublic(ChatMessage message, Principal principal) {
    if (principal == null) {
        throw new RuntimeException("Principal is null");
    }
    System.out.println("✅ Authenticated user sending message: " + principal.getName());
    message.setSender(principal.getName());
    message.setTimestamp(LocalDateTime.now());
    return message;
}



    @MessageMapping("/private")
    public void sendPrivate(ChatMessage chatMessage, Principal principal) {
        chatMessage.setSender(principal.getName());
        chatMessage.setTimestamp(LocalDateTime.now());
        messageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipient(), "/topic/private", chatMessage);
    }
}
