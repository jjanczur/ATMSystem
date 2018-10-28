package exceptions;

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
