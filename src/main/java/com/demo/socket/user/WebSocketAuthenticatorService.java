package com.demo.socket.user;

import lombok.RequiredArgsConstructor;
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
public class WebSocketAuthenticatorService {
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String  username, String password) throws AuthenticationException {
        if (Objects.isNull(username)|| username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        // Add your own logic for retrieving user in fetchUserFromDb()
        //fetchUserFromDb(username, password) == null
        if (!LOGIN_USERNAME.getValue().equals(username)) {
            throw new BadCredentialsException("Bad credentials for user " + username);
        }

        // null credentials, we do not pass the password along
        //TODO replace with token from UAA but we need to have GrantedAuthority anyway
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role(mandatory)
        );
    }

}
