package Backend.Entities.Items;

import Backend.Addons.EventLogger;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Discount.HiddenDiscount;
import Backend.Entities.Discount.VisibleDiscount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.DiscountType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import Exceptions.NotPremittedException;
import javafx.util.Pair;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "items")
public class Item implements Serializable {

    @Id
    @GeneratedValue
    private int item_id;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    private Store store;
    private double price;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Discount> discounts;
    private CategoryType category;

    public Item(String itemName, Store store, double price, CategoryType category) {
        this.name = itemName;
        this.store = store;
        this.price = price;
        this.category = category;
        this.discounts = new HashSet<>();
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public Store getStore() {
        return store;
    }

    public double getOriginalPrice() {
        return price;
    }

    public String getStoreName() {
        return store.getName();
    }

    public void changePrice(double newPrice) {
        this.price = newPrice;
    }

    public void addDiscount(String discountType, String startDate, String endDate, double percentOff) throws Exception {
        //check if discount already exist on those dates
        boolean canAdd = true;
        for (Discount discount : discounts) {
            if (discount.isInterfering(startDate, endDate) && discount.getType() == DiscountType.valueOf(discountType)) {
                canAdd = false;
            }
        }
        if (canAdd) {
            Discount toAdd = CreateDiscount(discountType, startDate, endDate, percentOff);
            if (toAdd != null) {
                discounts.add(toAdd);
                return;
            }
            throw new NotPremittedException("returned null");
        } else {
            throw new NotPremittedException("discount already exists");

        }
    }

    private Discount CreateDiscount(String discountType, String startDate, String endDate, double percentOff) throws ParseException {
        Discount toAdd = null;
        switch (DiscountType.valueOf(discountType)) {
            case VISIBLE:
                toAdd = new VisibleDiscount(startDate, endDate, percentOff);
                break;
            case HIDDEN:
                toAdd = new HiddenDiscount(startDate, endDate, percentOff);
                ((HiddenDiscount) toAdd).generateCouponCode();
                break;
        }
        return toAdd;
    }

    public void removeDiscount(String discountTypeString, String startDate, String endDate) throws Exception {
        DiscountType discountType = DiscountType.valueOf(discountTypeString);
        for (Discount discount : discounts) {
            if (discount.getType() == discountType && discount.isSameStartDate(startDate) && discount.isSameEndDate(endDate)) {
                discounts.remove(discount);
                return;
            }
        }
        throw new NotPremittedException("NO SUCH DISCOUNT FOUND!");
    }

    public CategoryType getCategory() {
        return category;
    }

    public double getPrice(String couponCode, double price) {
        double discountPercent = 0;
        double tmpDiscount;
        boolean foundVisible = false;
        boolean foundHidden = false;
        for (Discount discount : discounts) {
            if (!foundHidden && discount.getType() == DiscountType.HIDDEN) {
                tmpDiscount = discount.getDiscountPercent(couponCode);
                if (tmpDiscount > 0) {
                    discountPercent += tmpDiscount;
                    foundHidden = true;
                }
            }
            if (!foundVisible && discount.getType() == DiscountType.VISIBLE) {
                tmpDiscount = discount.getDiscountPercent(couponCode);
                if (tmpDiscount > 0) {
                    discountPercent += tmpDiscount;
                    foundVisible = true;
                }
            }
        }
        if (store.getCategoryDiscounts().containsKey(getCategory())) {
            Discounts discounts = this.store.getCategoryDiscounts().get(getCategory());
            for (Discount discount : discounts.getDiscountList()) {
                if (discount.getType() == DiscountType.HIDDEN) {
                    tmpDiscount = discount.getDiscountPercent(couponCode);
                    if (tmpDiscount > 0) {
                        discountPercent += tmpDiscount;
                    }
                }
                if (!foundVisible && discount.getType() == DiscountType.VISIBLE) {
                    tmpDiscount = discount.getDiscountPercent(couponCode);
                    if (tmpDiscount > 0) {
                        discountPercent += tmpDiscount;
                        foundVisible = true;
                    }
                }
            }
        }
        return price - (discountPercent / 100) * price;
    }

    public double getPrice() {
        return getPrice("0", this.price);
    }


    public double getPrice(Set<String> coupons) {
        double price = this.price;
        double new_price;
        {
            for (String couponCode : coupons) {
                new_price = getPrice(couponCode, price);
                if(new_price < price)
                    price = new_price;
            }
        }
        new_price = getPrice("0", price);
        if(new_price < price)
            price = new_price;
        return price;
    }

    public double checkout(User user, int numberToBuy, SaleType saleType, Set<Pair<String, String>> messages) throws Exception {
        double pricePerItem = getPrice(user.getUserCoupons());
        pricePerItem = store.checkout(user, this, numberToBuy, saleType, pricePerItem, messages);
        switch (saleType) {
            case RAFFLE:
                return numberToBuy / pricePerItem;
            case NORMAL:
                return pricePerItem * numberToBuy;
            default:
                throw new NotPremittedException("UNKOWN saletype " + saleType.toString());

        }
    }

    public void rollback(User user, Integer numberToBuy, SaleType saleType) throws Exception {
        double pricePerItem = getPrice(user.getUserCoupons());
        store.rollback(user, this, numberToBuy, saleType, pricePerItem);
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public int getNumberInStock() throws Exception {
        return store.getItemStock(name);
    }

    public double getPriceAfterDiscount() {
        return getPrice();
    }

    public double getPriceAfterDiscount(Set<String> coupons) {
        return getPrice(coupons);
    }

    public void editDiscountForItem(String newDiscountType, String newStartDate,
                                    String newEndDate, String newPercent, String oldDiscountType,
                                    String oldStartDate, String oldEndDate, String oldPercent)
            throws Exception {
        for (Discount discount : discounts) {
            if (discount.getType().equals(DiscountType.valueOf(oldDiscountType))
                    && discount.isSameStartDate(oldStartDate)
                    && discount.isSameEndDate(oldEndDate)
                    && discount.getDiscountPercent() == Double.parseDouble(oldPercent)) {
                if (!(DiscountType.valueOf(oldDiscountType).equals(DiscountType.valueOf(newDiscountType)))) {
                    this.removeDiscount(oldDiscountType, oldStartDate, oldEndDate);
                    this.addDiscount(newDiscountType, newStartDate, newEndDate, Double.parseDouble(newPercent));
                } else {
                    discount.setStartDate(newStartDate);
                    discount.setEndDate(newEndDate);
                    discount.setPercent(newPercent);
                }
            }
        }
    }
}
