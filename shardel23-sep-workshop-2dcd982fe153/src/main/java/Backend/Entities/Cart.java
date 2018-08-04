package Backend.Entities;

import Backend.Addons.EventLogger;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Exceptions.NoSuchItemException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CartItem> items;

    public Cart() {
        items = new HashSet<>();
    }

    public void addItem(Item storeItem, int amount, String saleType) {
        SaleType saleType1 = SaleType.valueOf(saleType);
        for(CartItem cartItem : items){
            if(cartItem.getItem().equals(storeItem) && saleType1.equals(cartItem.getSaleType())){
                cartItem.setAmount(cartItem.getAmount() + amount);
                return;
            }
        }
        items.add(new CartItem(storeItem, amount, saleType1));
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void removeItem(String itemName, String storeName, String saleType) throws Exception {
        CartItem found = null;
        SaleType type = SaleType.valueOf(saleType);
        for(CartItem cartItem : items){
            Item tripletItem = (Item) cartItem.getItem();
            if (tripletItem.getName().equals(itemName) && tripletItem.getStoreName().equals(storeName) && cartItem.getSaleType().equals(type)){
                found = cartItem;
            }
        }
        if(!items.remove(found)){
            throw new NoSuchItemException("ERROR REMOVING ITEM FROM CART");
        }
    }

    public void changeCartItemAmount(String itemName, String storeName, int newAmount, SaleType saletype) throws Exception {
        CartItem found = null;
        for(CartItem cartItem : items){
            Item tripletItem = (Item) cartItem.getItem();
            if (tripletItem.getName().equals(itemName) && tripletItem.getStoreName().equals(storeName) && cartItem.getSaleType().equals(saletype)){
                found = cartItem;
            }
        }
        if (found != null) {
            found.setAmount(newAmount);
        }
        else{
            EventLogger.errorLogger.info("changeCartItemAmount: no such item in cart");
            throw new NoSuchItemException("ERROR CHANGING CART ITEM AMOUNT");
        }
    }


    /**
     * Adds a coupon to the cart.
     * @param couponCode
     * @return the item that had a discount. null otherwise
     */
    public Item addCoupon(String couponCode){
        for (CartItem cartItem : items) {
            Item tripletItem = (Item) cartItem.getItem();
            if(tripletItem.getPrice()>tripletItem.getPrice(couponCode,tripletItem.getOriginalPrice()))
                return tripletItem;
        }
        return null;
	}
	
    public void addItem(CartItem item) {
        items.add(item);
    }
}
