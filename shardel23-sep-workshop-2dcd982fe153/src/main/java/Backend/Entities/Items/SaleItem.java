package Backend.Entities.Items;

import Backend.Entities.Enums.SaleType;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import Exceptions.CheckoutFailedException;
import Exceptions.NotPremittedException;
import javafx.util.Pair;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class SaleItem implements Serializable {

    @Id
    @GeneratedValue
    private int si_id;
    @OneToOne
    Item item;
    SaleType type;

    public SaleItem(Item item) {
        this.item = item;
        this.type = SaleType.NORMAL;
    }

    public SaleItem() {
    }

    public Item getItem() {
        return item;
    }

    public SaleType getType() {
        return type;
    }

    public void checkout(User user, int numberOfItems, Set<Pair<String, String>> messages) throws Exception {
        Store store = item.getStore();
        store.decrementItemStock(item.getName(), numberOfItems);
    }

    public void rollback(User user, int numberOfItems) throws Exception {
        Store store = item.getStore();
        store.incrementItemStock(item.getName(), numberOfItems);
    }

    public Date getStartDate() throws Exception  {
        throw new NotPremittedException("Not implemented") ;
    }
    public Date getEndDate()throws Exception {
        throw new NotPremittedException("Not implemented");
    }
    public int getAlreadyBought() throws Exception {
        throw new NotPremittedException("Not implemented");
    }
}

