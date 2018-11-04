package com.janczura.exceptions;

/**
 * This exception is thrown when the user is not  logged in and tries to perform any operation on the account or user  tries to login with wrong credentials
 */
public class ATMaccountException extends Exception{
    public ATMaccountException () {

    }

    public ATMaccountException (String message) {
        super (message);
    }

    public ATMaccountException (Throwable cause) {
        super (cause);
    }

    public ATMaccountException (String message, Throwable cause) {
        super (message, cause);
    }
}
