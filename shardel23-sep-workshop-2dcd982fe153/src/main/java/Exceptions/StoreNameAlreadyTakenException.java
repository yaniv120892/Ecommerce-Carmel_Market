package Exceptions;

import Backend.Addons.EventLogger;

public class StoreNameAlreadyTakenException extends Exception {

    public StoreNameAlreadyTakenException(String message) {
        super(message);
        EventLogger.errorLogger.info(message);
    }
}
