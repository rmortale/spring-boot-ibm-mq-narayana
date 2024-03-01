package com.example.springbootibmmq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WmqSender {

    private final JmsTemplate raiJmsTemplate;

    public void sendToWmqQueue(String body) {
        raiJmsTemplate.convertAndSend("DEV.QUEUE.2", body);
        if (body.equalsIgnoreCase("exception")) {
            throw new IllegalArgumentException("test");
        }
    }
}
