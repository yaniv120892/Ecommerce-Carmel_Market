package WebSocket;

import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/wsStats")
public class webSocketStats {

    Stats stats = Stats.getStats();

    @OnOpen
    public void onOpen(Session session) throws IOException {
//        for (int i = 0; i < 10; i++) {
//            stats.firstConnection();
//        }
//        for (int i = 0; i < 10; i++) {
//            stats.firstConnection();
//            stats.makeLogin("abc" + i);
//        }
        updateStats(session);
    }

    @OnClose
    public void onClose(Session session) {
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        updateStats(session);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    private void updateStats(Session session) throws IOException {
        Gson gson = new Gson();

        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

        ArrayList<String> firstArr = new ArrayList<>();
        firstArr.add("Year/Day");
        firstArr.add("Registered");
        firstArr.add("Guest");

        arrayList.add(firstArr);
        HashMap<Integer, HashMap<Integer, AtomicInteger>> reg = stats.getRegisteredUsers();

        for (HashMap.Entry<Integer, HashMap<Integer, AtomicInteger>> entry : stats.getGuests().entrySet()) {
            int year = entry.getKey();
            HashMap<Integer, AtomicInteger> dayAndNumGuest = entry.getValue();
            HashMap<Integer, AtomicInteger> dayAndNumReg = reg.get(year);

            for(HashMap.Entry<Integer,AtomicInteger> entry1 : dayAndNumGuest.entrySet()){
                int dayOfYear = entry1.getKey();
                int amoutnGuest = entry1.getValue().get();
                int amountReg = dayAndNumReg.get(dayOfYear).get();

                ArrayList toAdd = new ArrayList();
                toAdd.add(year + "/" + dayOfYear);
                toAdd.add(amountReg);
                toAdd.add(amoutnGuest);
                arrayList.add(toAdd);
            }
        }

        //arr.add(stats.getGuests());
        //arr.add(stats.getRegisteredUsers());

        String json = gson.toJson(arrayList);
        session.getBasicRemote().sendText(json);
    }
}
