package com.alianza.error;

import lombok.Getter;

@Getter
public enum Errors {

    INCORRECT_DATE("The final date cannot be lower than registry date"),
    INCORRECT_STRING("The name must be two words"),
    INCORRECT_EMAIL("Invalid email format");

    private final String message;

    Errors(String message) {
        this.message = message;
    }

}
