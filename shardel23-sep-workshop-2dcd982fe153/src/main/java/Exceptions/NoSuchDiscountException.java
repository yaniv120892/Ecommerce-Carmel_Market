package Exceptions;

import Backend.Addons.EventLogger;

public class NoSuchDiscountException extends Exception {
    public NoSuchDiscountException(String s) {
        super(s);
        EventLogger.errorLogger.info(s);
    }
}
