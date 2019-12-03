package com.demo.socket.controller;


import com.demo.socket.dto.Response;
import com.demo.socket.dto.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ChatController {

    private AtomicInteger atomicInt = new AtomicInteger(0);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Response greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        int counter = atomicInt.incrementAndGet();
        return new Response("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! message num" + counter);
    }
}