package com.pallavi.messaging_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class ChatMessage {

    public ChatMessage() {}

    @Id
    @GeneratedValue
    private Long id;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private String text;
    private String recipient;
}
