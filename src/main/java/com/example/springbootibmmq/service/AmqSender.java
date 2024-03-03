package com.example.springbootibmmq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmqSender {

    private final JmsTemplate vaiJmsTemplate;

    public void sendToAmqQueue(String body) {
        vaiJmsTemplate.convertAndSend("rai.from.queue.01", body);
        if (body.equalsIgnoreCase("exception")) {
            throw new IllegalArgumentException("test");
        }
    }

}
