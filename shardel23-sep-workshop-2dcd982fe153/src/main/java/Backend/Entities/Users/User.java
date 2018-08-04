package Backend.Entities.Users;

import Backend.Entities.Cart;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.NationalityBackup;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Enums.UserType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Policies.PurchasePolicy;
import Backend.Entities.Privileges;
import Backend.Entities.PurchaseHistory;
import Backend.Entities.Store;
import Exceptions.NoSuchDiscountException;
import Exceptions.NoSuchStoreException;
import Exceptions.NotPremittedException;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
@Table(name = "users")
public abstract class User implements Comparable<User> {
    @Id
    private String email;
    private String hashedPassword;
    UserType type;
    private NationalityBackup nationality;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> userCoupons;



    public User() {
        this.email = "";
        this.hashedPassword = "";
        this.type = UserType.GUEST;
        this.nationality = NationalityBackup.ISRAEL;
        this.userCoupons = new HashSet<String>();
    }

    /**
     * Adds a coupon if is valid and doesn't exist in the list
     * @param couponCode
     */
    public void addCoupon(String couponCode) throws Exception {
        if (couponCode.length() != 9)
            throw new NotPremittedException("Coupon is not 9 characters long");
        if(this.userCoupons.contains(couponCode))
            throw new NotPremittedException("Coupon code already exists, Duplication is not allowed ");
        this.userCoupons.add(couponCode);
    }

    /**
     * Removes a coupon from the user coupons
     * @param couponCode
     * @return true if the coupon was found. false otherwise
     */
    public boolean removeCoupon(String couponCode){
        if(this.userCoupons.contains(couponCode)){
            this.userCoupons.remove(couponCode);
            return true;
        }
        return false;
    }

    public void deleteAllCoupons() {
        this.userCoupons = new HashSet<>();
    }

    public Set<String> getUserCoupons() {
        return this.userCoupons;
    }

    public String getEmail() {
        return this.email;
    }

    public UserType getType() {
        return this.type;
    }

    public abstract void setType(UserType registered);

    public abstract void addToCart(Item storeItem, int amount, String saleType) throws Exception;

    public abstract Set<CartItem> getMyCartItems() throws Exception;

    public abstract void removeFromCart(String itemName, String storeName, String saleType) throws Exception;

    public abstract void changeCartItemAmount(String itemName, String storeName, int newAmount, SaleType raffle) throws Exception;

    public abstract void addStoreAsOwner(Store newStore) throws Exception;

    public abstract void addItemToStore(String itemName, String storeName, int initialAmountInStock, double price, String category) throws Exception;

    public abstract void removeItemFromStore(String itemName, String storeName) throws Exception;

    public abstract void editStoreItemPrice(String itemName, String storeName, double newPrice) throws Exception;

    public abstract void editStoreItemStock(String itemName, String storeName, int newAmount) throws Exception;

    public abstract void editStoreItemIsOnSale(String itemName, String storeName, boolean isOnSale) throws Exception;

    public abstract void addItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate, String percentOff) throws Exception;

    public abstract void removeItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate) throws Exception;

    public abstract void appointNewOwner(String storeName, User user) throws Exception;

    public abstract void appointManager(User user, String storeName, boolean[] privileges) throws Exception;

    public Set<PurchaseHistory> getStorePurchaseHistory(Store store) throws Exception {
        return store.getPurchaseHistory();
    }

    public abstract boolean removeUserFromSystem(String toDeleteUserEmail) throws Exception;

    public abstract Map<Store, Privileges> getStores() throws Exception;

    public abstract Store getStore(String storeName) throws Exception;

    public abstract Set<PurchaseHistory> getPurchaseHistoryOfStore(String storeName) throws Exception;

    public String getHashedPW() {
        return hashedPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.hashedPassword = password;
    }

    public abstract void rollbackCart(Cart futureRollback) throws Exception;

    public abstract Pair<Double, Set<Pair<String, String>>>  checkout(String userID, PurchasePolicy systemPP, HashMap<CategoryType, PurchasePolicy> PpPerCategory, String address) throws Exception;

    public abstract void clearCart() throws Exception;

    public abstract Set<PurchaseHistory> getPurchaseHistory() throws Exception;

    public abstract void deleteStore(Store store) throws NotPremittedException;

    public abstract boolean isOwnerOrManager(String storeName) throws Exception;

    public abstract  Cart getCart() throws Exception;

    public abstract void removeSaleItemFromStore(String currStoreName, String itemName, String saleType) throws Exception;

    public abstract void addItemToSale(String storeName, String itemName, String saleType, String startDate, String endDate) throws Exception;

    public abstract void removeCategoryDiscountFromStore(String storeName, String name,String type, String start, String end) throws Exception;

    public abstract void addCategoryDiscountToStore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception, NoSuchDiscountException;

    public abstract void editCategoryDiscountToScore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception;

    public abstract Set<Discount> getItemDiscounts(String store, String item) throws Exception;

    public abstract void addPurchaseRule(String itemName, boolean additionalNotOperator, String operator, String ruleType, Integer numberDetail, String stringDetail, String storeName, String categoryName, PurchasePolicy purchasePolicyForAdmin) throws Exception;

    public abstract void removePurchaseRule(String itemName, String storeName, String categoryName, PurchasePolicy purchasePolicyForAdmin) throws Exception;

    public NationalityBackup getNationality() {
        return nationality;
    }

    public void setNationality(NationalityBackup nationality) {
        this.nationality = nationality;
    }

    public abstract void editDiscountForItem(String itemName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception;

    public abstract void closeStore(String storeName) throws NoSuchStoreException, NotPremittedException;

    @Override
    public int compareTo(User o) {
        if(this.email.equals(o.email)){
            return 0;
        }
        return 1;
    }

    public abstract Privileges getStorePrivileges(String name) throws Exception;

    public String toString()
    {
        return this.email;
    }

    public abstract void editCategoryDiscountToScore(String categoryName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate,
                                                     String oldEndDate, String oldPercent
                                                     ) throws Exception;
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setUserCoupons(Set<String> userCoupons) {
        this.userCoupons = userCoupons;
    }
}
