package com.demo.socket.service;


import com.demo.socket.dto.Response;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@AllArgsConstructor
public class ChatBotTimeNotifier {

    private SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 3000)
    public void publishUpdates() {
        Response payload = new Response("Hello! Local time is: " + LocalTime.now());
        template.convertAndSend("/topic/greetings", payload);
    }

}
