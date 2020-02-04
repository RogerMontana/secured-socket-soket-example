package com.demo.socket.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecurityConst {
    LOGIN_USERNAME("user"),

    USERNAME_HEADER("login"),
    PASSWORD_HEADER ("passcode");
    String value;
}