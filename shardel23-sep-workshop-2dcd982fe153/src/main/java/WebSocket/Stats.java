package WebSocket;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

    //<YEAR, <DAY_OF_YEAR, NUMBER OF CONNECTED USERS>>
    private HashMap<Integer, HashMap<Integer, AtomicInteger>> registeredUsers;
    private HashMap<Integer, HashMap<Integer, AtomicInteger>> guests;
    private Calendar calendar = Calendar.getInstance();

    private static Stats stats = new Stats();

    public static Stats getStats() {
        return stats;
    }

    private Stats() {
        this.registeredUsers = new HashMap<>();
        this.guests = new HashMap<>();
    }


    public void firstConnection() {
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        HashMap<Integer, AtomicInteger> inner = guests.computeIfAbsent(year, k -> new HashMap<>());
        inner.computeIfAbsent(dayOfYear, k -> new AtomicInteger()).incrementAndGet();
    }

    public void makeLogin(String email) {
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        HashMap<Integer, AtomicInteger> inner = registeredUsers.computeIfAbsent(year, k -> new HashMap<>());
        inner.computeIfAbsent(dayOfYear, k -> new AtomicInteger()).incrementAndGet();
        guests.get(year).get(dayOfYear).decrementAndGet();
    }

    public HashMap<Integer, HashMap<Integer, AtomicInteger>> getRegisteredUsers() {
        return registeredUsers;
    }

    public HashMap<Integer, HashMap<Integer, AtomicInteger>> getGuests() {
        return guests;
    }
}
