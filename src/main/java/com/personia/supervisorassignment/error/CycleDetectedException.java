package com.personia.supervisorassignment.error;

public class CycleDetectedException extends RuntimeException {
    public CycleDetectedException() {
        super();
    }

    public CycleDetectedException(String message) {
        super(message);
    }
}
