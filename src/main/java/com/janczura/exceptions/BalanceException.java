package com.janczura.exceptions;

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
