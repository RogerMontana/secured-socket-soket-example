package com.demo.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
//@EnableScheduling
@EnableWebSocketMessageBroker
public class SocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketApplication.class, args);
	}

}
