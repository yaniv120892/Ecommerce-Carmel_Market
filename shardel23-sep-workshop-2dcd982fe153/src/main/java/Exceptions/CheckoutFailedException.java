package Exceptions;

import Backend.Addons.EventLogger;

public class CheckoutFailedException extends Exception{

    public CheckoutFailedException(String message) {
        super(message);
        EventLogger.errorLogger.info(message);
    }
}
