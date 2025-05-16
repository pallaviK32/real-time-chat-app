package com.pallavi.messaging_service.repository;

import com.pallavi.messaging_service.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
