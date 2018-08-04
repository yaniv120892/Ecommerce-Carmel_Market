package Backend.Entities.Items;


import Backend.Addons.EventLogger;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import Exceptions.CheckoutFailedException;
import javafx.util.Pair;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;

@Entity
public class RaffleItem extends SaleItem {

    private Date startDate;
    private Date endDate;
    AtomicInteger alreadyBought;
    @ElementCollection(fetch = FetchType.EAGER)
    Map<String, Integer> users;

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public RaffleItem(Item item, String startDate, String endDate) throws ParseException {
        super(item);
        this.alreadyBought = new AtomicInteger(0);
        users = new ConcurrentHashMap<String, Integer>();
        type = SaleType.RAFFLE;
        this.startDate = df.parse(startDate);
        this.endDate = df.parse(endDate);
    }

    public RaffleItem() {
    }

    @Override
    public void checkout(User user, int percentToBuy, Set<Pair<String, String>> messages) throws Exception {
        if (alreadyBought.compareAndSet(0, percentToBuy)) {
            Store store = item.getStore();
            store.decrementItemStock(item.getName(), 1);
        }else{
            int after = alreadyBought.addAndGet(percentToBuy);
            if(after > 100){
                alreadyBought.addAndGet(percentToBuy * -1);
                throw new CheckoutFailedException("percent is above 100");
            }
            users.put(user.getEmail(), percentToBuy);
        }
        if (alreadyBought.get() == 100) {
            String msg = "RAFFLE for item: " + item.getName() + " from store: " + item.getStoreName() + " is finished";
            String winnerMsg = "YOU ARE THE WINNER FOR RAFFLE for item: " + item.getName() + " from store: " + item.getStoreName() + " is finished";
            Random rand = new Random();
            String winner = "";
            int n = rand.nextInt(100) + 1;
            int tot = 0;
            for (Entry<String, Integer> pair : users.entrySet()) {
                if (tot + pair.getValue() >= n) {
                    //winner!!!
                    winner = pair.getKey();
                    break;
                }
                tot += pair.getValue();
            }


            for (Entry<String, Integer> pair : users.entrySet()) {
                if (!pair.getKey().equals(winner)) {
                    messages.add(new Pair<>(pair.getKey(), msg));
                }
            }
            messages.add(new Pair<>(winner, winnerMsg));
        }
    }

    public boolean checkEndTime() {
        Date date = new Date();
        return date.compareTo(endDate) >= 0;
    }

    public List<String> getUsersInRaffle() {
        List<String> ans = new ArrayList<>();
        for (Entry<String, Integer> pair : users.entrySet()) {
            ans.add(pair.getKey());
        }
        return ans;
    }

    @Override
    public void rollback(User user, int percentToBuy) throws Exception {
        Store store = item.getStore();
        if (alreadyBought.get() == 0) {
            store.incrementItemStock(item.getName(), 1);
        }
        for (Entry<String, Integer> pair : users.entrySet()) {
            if (pair.getKey().equals(user.getEmail())) {
                users.remove(pair);
                return;
            }
        }
    }

    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public int getAlreadyBought() {
        return alreadyBought.get();
    }
}
