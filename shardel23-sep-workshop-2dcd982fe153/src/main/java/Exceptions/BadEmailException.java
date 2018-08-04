package Exceptions;


import Backend.Addons.EventLogger;

public class BadEmailException extends Exception {
    public BadEmailException(String s) {
        super(s);
        EventLogger.errorLogger.info(s);
    }
}
