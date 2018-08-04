package Exceptions;

import Backend.Addons.EventLogger;

public class BadArgumentException extends Exception{

    public BadArgumentException(String argName) {
        super("Something is wrong with the argument " + argName + ".");
        EventLogger.errorLogger.info("Something is wrong with the argument " + argName + ".");
    }

}
