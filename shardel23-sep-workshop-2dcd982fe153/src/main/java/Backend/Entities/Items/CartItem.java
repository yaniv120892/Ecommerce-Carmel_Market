package Backend.Entities.Items;

import Backend.Entities.Enums.SaleType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cartItems")
public class CartItem implements Serializable {

    @Id
    @GeneratedValue
    private int ci_id;
    @OneToOne
    private Item item;
    private int amount;
    private SaleType saleType;

    public CartItem(Item item, int amount, SaleType saleType) {
        this.item = item;
        this.amount = amount;
        this.saleType = saleType;
    }

    public CartItem() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }
}
