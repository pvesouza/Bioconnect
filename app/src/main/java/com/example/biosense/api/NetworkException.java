package com.example.biosense.api;

import java.io.IOException;

public class NetworkException extends IOException {
    private String message;
    NetworkException(String e) {
        this.message = e;
    }

    public String getMessage() {
        return this.message;
    }
}
