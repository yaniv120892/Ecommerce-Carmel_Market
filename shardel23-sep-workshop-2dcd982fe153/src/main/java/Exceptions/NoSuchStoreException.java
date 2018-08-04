package Exceptions;

import Backend.Addons.EventLogger;

public class NoSuchStoreException extends Exception {

    public NoSuchStoreException(String message){
        super(message);
        EventLogger.errorLogger.info(message);
    }
}
