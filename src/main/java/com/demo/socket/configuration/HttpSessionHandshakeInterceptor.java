package com.demo.socket.configuration;

import com.demo.socket.constant.SecurityConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {

            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();

            Optional<Cookie> token = Optional.ofNullable(WebUtils.getCookie(servletRequest, "token"));

            String extractedToken;
            if (token.isPresent()) {
                extractedToken = token.get().getValue();
                log.info("extracted token is {}", extractedToken);
            } else {
                extractedToken = SecurityConst.MOCK_USER_TOKEN.getValue();
                log.info("mocked token is {}", extractedToken);
            }

            attributes.put("token", extractedToken);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            //no callbacks
    }
}
