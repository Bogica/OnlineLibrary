package com.example.library.exception;

public class LimitedNumberException extends RuntimeException {
    public LimitedNumberException(String message) {
        super(message);
    }
}