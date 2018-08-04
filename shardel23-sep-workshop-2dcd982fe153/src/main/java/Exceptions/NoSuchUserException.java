package Exceptions;

import Backend.Addons.EventLogger;

public class NoSuchUserException extends Exception{

    public NoSuchUserException() {
        super("No such user in the system");
        String msg = "No such user in the system";
        EventLogger.errorLogger.info(msg);
    }

}
