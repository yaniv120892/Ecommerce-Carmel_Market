package WebSocket;


import Backend.Addons.EventLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/ws", configurator = GetHttpSessionConfigurator.class)
public class webSocket {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        Publisher publisher = Publisher.getPublisher();
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String userID = (String) httpSession.getAttribute("userID");
        publisher.getUsers().put(userID, session);
        System.out.println("userID logging in: " + userID);

        //checking if he has missed msgs
        if (publisher.getToSend().get(userID) != null) {
            for (String msg : publisher.getToSend().get(userID).getMessages()) {
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //everything was send, remove from list
            publisher.getToSend().remove(userID);
        }
    }

    @OnClose
    public void onClose(Session session) {
        Publisher publisher = Publisher.getPublisher();
        for (Map.Entry<String, Session> s : publisher.getUsers().entrySet()) {
            if (s.getValue().equals(session)) {
                System.out.println("userID logging out: " + s.getKey());
                publisher.getUsers().remove(s.getKey());
                return;
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }


}
