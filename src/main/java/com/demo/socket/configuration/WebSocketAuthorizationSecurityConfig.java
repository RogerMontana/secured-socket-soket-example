package com.demo.socket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketAuthorizationSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
        // TODO [Messaging Security] customize your authorization mapping here.
        messages
                // message types other than MESSAGE and SUBSCRIBE
                .nullDestMatcher().authenticated()
                // matches any destination that need to be secured
                .simpDestMatchers("/user/queue/topic/**").authenticated()
                .simpDestMatchers("/queue/topic/**").authenticated()
                .simpDestMatchers("/app/**").authenticated()
                // (i.e. cannot send messages directly to /topic/, /queue/)
                // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
                // /topic/messages-user<id>)
                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
                // catch all
                .anyMessage().denyAll();

    }

    // TODO: For test purpose (and simplicity) i disabled CSRF, but you should re-enable this and provide a CRSF endpoint.
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
