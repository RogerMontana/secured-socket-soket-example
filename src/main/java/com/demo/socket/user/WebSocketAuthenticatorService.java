package com.demo.socket.user;

import com.demo.socket.constant.SecurityConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

import static com.demo.socket.constant.SecurityConst.LOGIN_USERNAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthenticatorService {

    public static final String USERNAME_WAS_NULL_OR_EMPTY = "Username was null or empty.";
    public static final String PASSWORD_WAS_NULL_OR_EMPTY = "Password was null or empty.";

    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String  username, String password, String token) throws AuthenticationException {
        if (Objects.isNull(username)|| username.trim().isEmpty()) {
            log.error(USERNAME_WAS_NULL_OR_EMPTY);
            throw new AuthenticationCredentialsNotFoundException(USERNAME_WAS_NULL_OR_EMPTY);
        }
        if (password == null || password.trim().isEmpty()) {
            log.error(PASSWORD_WAS_NULL_OR_EMPTY);
            throw new AuthenticationCredentialsNotFoundException(PASSWORD_WAS_NULL_OR_EMPTY);
        }
        if (!SecurityConst.USER_TOKEN.equals(token)) {
            log.error("User Token INVALID token value: {}", token);
            //throw new BadCredentialsException("Bad token for user " + username);
        }
        // Add your own logic for retrieving user in fetchUserFromDb()
        //fetchUserFromDb(username, password) == null
        if (!LOGIN_USERNAME.getValue().equals(username)) {
            log.error("Bad credentials for user : {}", username);
            throw new BadCredentialsException("Bad credentials for user " + username);
        }

        // null credentials, we do not pass the password along
        //TODO replace with token from UAA but we need to have GrantedAuthority anyway
        return new UsernamePasswordAuthenticationToken(
                username,
                token,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role(mandatory)
        );
    }

}
