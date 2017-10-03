package com.babcock.integration.asserter;

public class WaitUntilAssertionError extends RuntimeException {

    public WaitUntilAssertionError(final String message) {
        super(message);
    }
}
