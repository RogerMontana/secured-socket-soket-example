package com.demo.socket.configuration;

import com.demo.socket.user.WebSocketAuthenticatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.demo.socket.constant.SecurityConst.PASSWORD_HEADER;
import static com.demo.socket.constant.SecurityConst.USERNAME_HEADER;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (isCommand(accessor, StompCommand.CONNECT) || isCommand(accessor, StompCommand.SEND)) {
            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER.getValue());
            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER.getValue());

            Map<String, Object> authorization = accessor.getSessionAttributes();

            String token = (String) authorization.get("token");
            log.info("authorization attributes are {} and have token like {}", authorization, token);
            final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password, token);

            accessor.setUser(user);
        }
        return message;
    }

    private boolean isCommand(StompHeaderAccessor accessor, StompCommand connect) {
        return connect == accessor.getCommand();
    }
}
