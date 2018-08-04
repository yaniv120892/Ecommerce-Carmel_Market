package Backend.Addons;

import java.util.logging.Logger;

public class EventLogger {

    // assumes the current class is called MyLogger
    //public final static Logger LOGGER = Logger.getLogger("manager.org.apache.juli.AsyncFileHandler");
    public final static Logger eventLogger = Logger.getLogger("org.apache.catalina.core.ContainerBase.[Catalina].[localhost].[/eventlog]");
    public final static Logger errorLogger = Logger.getLogger("org.apache.catalina.core.ContainerBase.[Catalina].[localhost].[/errorlog]");
}
