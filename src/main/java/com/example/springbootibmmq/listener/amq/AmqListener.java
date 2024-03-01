package com.example.springbootibmmq.listener.amq;

import com.example.springbootibmmq.service.WmqSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AmqListener {

    private final WmqSender sender;

    @Transactional
    @JmsListener(destination = "rai.to.queue.01", concurrency = "10", containerFactory = "vaiListenerContainerFactory")
    public void handleMessage(String body) {
        log.info("Received msg: {}", body);
        sender.sendToWmqQueue(body);
    }
}
