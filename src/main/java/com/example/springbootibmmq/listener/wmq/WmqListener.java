package com.example.springbootibmmq.listener.wmq;

import com.example.springbootibmmq.service.AmqSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WmqListener {

    private final AmqSender sender;

    @Transactional
    @JmsListener(destination = "DEV.QUEUE.2", containerFactory = "raiListenerContainerFactory", concurrency = "5")
    public void handleMessage(String body) {
        log.info("Received msg: {}", body.substring(0, 30));
        sender.sendToAmqQueue(body);
    }
}
