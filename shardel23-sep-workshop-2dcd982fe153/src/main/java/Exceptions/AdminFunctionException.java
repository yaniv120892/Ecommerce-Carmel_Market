package Exceptions;

import Backend.Addons.EventLogger;

public class AdminFunctionException extends Exception{

    public AdminFunctionException() {
        super("Admin function is not set!");
        String msg = "Admin function is not set!";
        EventLogger.errorLogger.info(msg);
    }

}
