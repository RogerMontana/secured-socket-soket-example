package com.demo.socket.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecurityConst {
    LOGIN_USERNAME("user"),
    USER_TOKEN("1111"),
    MOCK_USER_TOKEN("NO_TOKEN"),

    USERNAME_HEADER("login"),
    PASSWORD_HEADER ("passcode");
    String value;
}