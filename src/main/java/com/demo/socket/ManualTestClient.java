package com.demo.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class ManualTestClient {
    private static WebSocketClient client;
    private static WebSocketStompClient stompClient;
    // private StompSessionHandler sessionHandler;
    private static StompSession stompSession;
    private static SockJsClient sockJsClient;


    public static void main(String[] args) {
        try {
            initWSConnectionTS();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", "user");
        connectHeaders.add("passcode", "passwd");

        String url = "ws://localhost:8080";
        stompClient.connect(url, new WebSocketHttpHeaders(), connectHeaders, new TestSessionHandler());
    }

    public static WebSocketClient initWSConnectionTS() throws ExecutionException, InterruptedException {

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
        container.setDefaultMaxTextMessageBufferSize(1024 * 1024);
        WebSocketClient client = new StandardWebSocketClient(container);

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(client));
        sockJsClient = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);
        String url = "ws://localhost:8080/app";


        stompSession = stompClient.connect(url, new TestSessionHandler()).get();

        return client;
    }

    public static class TestSessionHandler implements StompSessionHandler {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.setAutoReceipt(true);
            session.subscribe("/app", this);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {

        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {

        }
    }
}
