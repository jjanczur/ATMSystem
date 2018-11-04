package com.janczura.exceptions;

/**
 * This exception is thrown on every attempt  setting up the balance below zero
 */
public class BalanceException extends Exception {

    public BalanceException () {

    }

    public BalanceException (String message) {
        super (message);
    }

    public BalanceException (Throwable cause) {
        super (cause);
    }

    public BalanceException (String message, Throwable cause) {
        super (message, cause);
    }
}
