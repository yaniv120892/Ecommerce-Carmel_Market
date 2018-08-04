package Backend.Entities.Discount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Discounts {

    @Id
    private int discounts_id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Discount> discountList;

    public Discounts(Set<Discount> discountList) {
        this.discountList = discountList;
    }

    public Discounts() {
        discountList = new HashSet<>();
    }

    public Set<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(Set<Discount> discountList) {
        this.discountList = discountList;
    }

    public void add(Discount d) {
        discountList.add(d);
    }

    public void remove(Discount discount) {
        discountList.remove(discount);
    }
}
