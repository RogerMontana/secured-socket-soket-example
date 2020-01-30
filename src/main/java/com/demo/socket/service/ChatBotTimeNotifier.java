package com.demo.socket.service;


import com.demo.socket.constant.TopicNames;
import com.demo.socket.dto.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@AllArgsConstructor
@Slf4j
public class ChatBotTimeNotifier {

    private final SimpMessagingTemplate simpMessagingTemplate;

//    @Scheduled(fixedDelay = 3000)
    public void publishUpdates() {
        Response payload = new Response("Hello! Local time is: " + LocalTime.now());
        log.info("ScheduledMessage {}", payload);
        simpMessagingTemplate.convertAndSend(TopicNames.TOPIC.getValue(), payload);
    }

}
