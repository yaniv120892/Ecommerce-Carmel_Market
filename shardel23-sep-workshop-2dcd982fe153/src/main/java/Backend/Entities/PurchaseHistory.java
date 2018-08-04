package Backend.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "PURCHASE_HISTORIES")
public class PurchaseHistory {

    private static AtomicInteger idCounter = new AtomicInteger();
    @Id
    private int id;
    private String itemName;
    private String userEmail;
    private String storeName;
    private int amountBought;
    private double cost;
    private String description;
    private String date;

    public PurchaseHistory(String itemName, String userEmail, String storeName,int amountBought, double cost, String description) {
        this.id = idCounter.incrementAndGet();
        this.itemName = itemName;
        this.userEmail = userEmail;
        this.storeName = storeName;
        this.amountBought = amountBought;
        this.cost = cost;
        this.description = description;
        this.date = LocalDateTime.now().toString();
    }

    public PurchaseHistory() {
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getAmountBought() {
        return amountBought;
    }

    public double getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setAmountBought(int amountBought) {
        this.amountBought = amountBought;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
