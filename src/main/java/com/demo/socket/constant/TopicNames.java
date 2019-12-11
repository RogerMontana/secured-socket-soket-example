package com.demo.socket.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TopicNames {
    TOPIC("/queue/topic/greetings");
    String value;
}
