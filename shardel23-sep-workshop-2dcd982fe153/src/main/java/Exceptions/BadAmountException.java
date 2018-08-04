package Exceptions;

import Backend.Addons.EventLogger;

public class BadAmountException extends Exception{

    public BadAmountException() {
        super("Bad amount exception");
        String msg = "Bad amount exception";
        EventLogger.errorLogger.info(msg);
    }

}
