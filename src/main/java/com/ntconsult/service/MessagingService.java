package com.ntconsult.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final JmsTemplate jmsTemplate;

    public MessagingService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String topicName, String message) {
        jmsTemplate.convertAndSend(topicName, message);
    }
}
