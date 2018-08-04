package Exceptions;

import Backend.Addons.EventLogger;

public class BadPasswordException extends Exception {

    public BadPasswordException(String s) {
        super(s);
        EventLogger.errorLogger.info(s);
    }
}
