package io.espens.asteroids.controller;

public class AsteroidsException extends Exception {
    public AsteroidsException(String msg) {
        super(msg);
    }

    public AsteroidsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
