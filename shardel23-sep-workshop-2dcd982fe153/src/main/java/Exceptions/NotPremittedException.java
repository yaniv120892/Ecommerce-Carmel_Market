package Exceptions;

import Backend.Addons.EventLogger;

public class NotPremittedException extends Exception {

    public NotPremittedException(String message) {
        super(message);
        EventLogger.errorLogger.info(message);
    }
}
