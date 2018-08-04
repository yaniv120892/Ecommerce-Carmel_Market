package WebSocket;

import javax.persistence.*;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
public class Publisher {

    @Id
    @GeneratedValue
    private int p_id;
    @Transient
    //userID , WS-Session
    private Map<String, Session> users = new ConcurrentHashMap<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //UserID, msgs
    private Map<String, Messages> toSend = new HashMap<>();

    public Publisher() {

    }

    private static Publisher publisher = new Publisher();

    public static Publisher getPublisher() {
        return publisher;
    }

    public Map<String, Session> getUsers() {
        return users;
    }

    public Map<String, Messages> getToSend() {
        return toSend;
    }

    public void sendAll(String msg) throws IOException {
        for (Map.Entry<String, Session> s : getUsers().entrySet()) {
            s.getValue().getBasicRemote().sendText(msg);
        }
    }

    public void sendToUserGroup(List<String> userList, String msg) throws IOException {
        for (String user : userList) {
            sendToUser(user, msg);
        }
    }

    public void sendToUser(String user, String msg) throws IOException {
        Session curSession = getUsers().get(user);
        if (curSession != null) {
            //user is online
            curSession.getBasicRemote().sendText(msg);
        } else {
            //user not online
            if (isInteger(user)) {
                //guest
            } else {
                //registered
                if (getToSend().get(user) != null) {
                    //has more missed messages
                    getToSend().get(user).add(msg);
                } else {
                    //he is not in the array
                    Messages newArr = new Messages();
                    //ArrayList<String> newArr = new ArrayList<>();
                    newArr.add(msg);
                    getToSend().put(user, newArr);
                }

            }
        }
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
