package com.personia.supervisorassignment.error;

public class MultipleRootsDetectedException extends RuntimeException {
    public MultipleRootsDetectedException() {
        super();
    }

    public MultipleRootsDetectedException(String message) {
        super(message);
    }
}
