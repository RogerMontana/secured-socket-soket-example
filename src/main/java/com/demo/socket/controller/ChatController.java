package com.demo.socket.controller;


import com.demo.socket.constant.TopicNames;
import com.demo.socket.dto.HelloMessage;
import com.demo.socket.dto.Response;
import com.demo.socket.user.WebSocketAuthenticatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "passcode";

    private AtomicInteger atomicInt = new AtomicInteger(0);

    @MessageMapping("/hello")
    public void greeting(@Payload HelloMessage message, @Header("simpSessionId") String sessionId) throws Exception {
        Thread.sleep(1000); // simulated delay
        int counter = atomicInt.incrementAndGet();

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        final String username = headerAccessor.getFirstNativeHeader(USERNAME_HEADER);
        final String password = headerAccessor.getFirstNativeHeader(PASSWORD_HEADER);

        final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);

        headerAccessor.setUser(user);

        Response response = new Response("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! message num" + counter);

        log.info(response.toString());
        log.info("Send to session: {}, message {}", sessionId, response);

        simpMessagingTemplate.convertAndSendToUser(sessionId, TopicNames.TOPIC.getValue(), response, headerAccessor.getMessageHeaders());
    }
}
