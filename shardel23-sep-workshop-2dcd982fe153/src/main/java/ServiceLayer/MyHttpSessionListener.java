package ServiceLayer;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyHttpSessionListener implements HttpSessionListener {

    ServiceProvider sp;
    public MyHttpSessionListener() {
        super();
        sp = ServiceProvider.getSP();
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //System.out.println(httpSessionEvent.getSession().getAttribute("userID"));
        httpSessionEvent.getSession().setMaxInactiveInterval(60*15);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String userID = (String) httpSessionEvent.getSession().getAttribute("userID");
        sp.removeInSystemUser(userID);
    }
}
