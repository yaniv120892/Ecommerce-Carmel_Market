package Exceptions;

import Backend.Addons.EventLogger;

public class NoSuchItemException extends Exception {
    public NoSuchItemException(String s) {
        super(s);
        EventLogger.errorLogger.info(s);
    }
}
