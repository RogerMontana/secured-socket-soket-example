package com.demo.socket.configuration;

import com.demo.socket.user.WebSocketAuthenticatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static com.demo.socket.constant.SecurityConst.PASSWORD_HEADER;
import static com.demo.socket.constant.SecurityConst.USERNAME_HEADER;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (isCommand(accessor, StompCommand.CONNECT) || isCommand(accessor, StompCommand.SEND)) {
            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER.getValue());
            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER.getValue());

            final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);

            accessor.setUser(user);
        }
        return message;
    }

    private boolean isCommand(StompHeaderAccessor accessor, StompCommand connect) {
        return connect == accessor.getCommand();
    }
}
