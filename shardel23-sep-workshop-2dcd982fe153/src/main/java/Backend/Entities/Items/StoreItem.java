package Backend.Entities.Items;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class StoreItem implements Serializable {

    @Id
    @GeneratedValue
    private int si_id;
    private boolean isOnSale;
    private AtomicInteger amount;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Item item;

    public StoreItem() {
    }

    public StoreItem(boolean isOnSale, int amount, Item item) {
        this.isOnSale = isOnSale;
        this.amount = new AtomicInteger(amount);
        this.item = item;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int addAndGet(int amount) {
        return this.amount.addAndGet(amount);
    }
}
