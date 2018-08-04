package Backend.Entities.Users;

import Backend.Data.DBInterface;
import Backend.Entities.Cart;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Enums.UserType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Policies.PurchasePolicy;
import Backend.Entities.Privileges;
import Backend.Entities.PurchaseHistory;
import Backend.Entities.Store;
import Exceptions.AdminFunctionException;
import Exceptions.BadArgumentException;
import Exceptions.NoSuchStoreException;
import Exceptions.NoSuchUserException;
import javafx.util.Pair;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Entity
public class AdminUser extends User {

    @Transient
    DBInterface data;

    //how do you add admins to the system?!?!
    public AdminUser(DBInterface data) {
        this.type = UserType.ADMIN;
        this.data = data;
    }

    public AdminUser() {
    }

    @Override
    public UserType getType() {
        return this.type;
    }

    @Override
    public void setType(UserType registered) {
        this.type = registered;
    }

    @Override
    public void addToCart(Item storeItem, int amount, String saleType) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public Set<CartItem> getMyCartItems() throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void removeFromCart(String itemName, String storeName, String saleType) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void changeCartItemAmount(String itemName, String storeName, int newAmount, SaleType raffle) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void addStoreAsOwner(Store newStore) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void addItemToStore(String itemName, String storeName, int initialAmountInStock, double price, String category) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void removeItemFromStore(String itemName, String storeName) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void editStoreItemPrice(String itemName, String storeName, double newPrice) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void editStoreItemStock(String itemName, String storeName, int newAmount) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void editStoreItemIsOnSale(String itemName, String storeName, boolean isOnSale) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void addItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate, String percentOff) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void removeItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void appointNewOwner(String storeName, User user) throws Exception {
        throw new AdminFunctionException();
    }


    @Override
    public void appointManager(User user, String storeName, boolean[] privileges) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public Set<PurchaseHistory> getPurchaseHistoryOfStore(String storeName) throws Exception {
        Store store = data.getStoreByName(storeName);
        return super.getStorePurchaseHistory(store);
    }


    @Override
    public void rollbackCart(Cart futureRollback) throws Exception {
        throw new AdminFunctionException();
    }

    public Pair<Double, Set<Pair<String, String>>> checkout(String userID, PurchasePolicy systemPP, HashMap<CategoryType, PurchasePolicy> PpPerCategory, String address) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void clearCart() throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public Set<PurchaseHistory> getPurchaseHistory() throws Exception {
        throw new AdminFunctionException();
    }

    public boolean isOwnerOrManager(String storeName) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void deleteStore(Store store) {
        //TODO:
        store.setIsActive(false);
    }

    @Override
    public boolean removeUserFromSystem(String toDeleteUserEmail) throws Exception {
        User toDelete = data.getUser(toDeleteUserEmail);
        if (toDelete == null)
            throw new NoSuchUserException();
        else {

            if (toDelete.getType() == UserType.REGISTERED) {
                //TODO: check orphan stores
                for (Entry<Store, Privileges> pair : toDelete.getStores().entrySet()) {
                    if (pair.getKey().getOwners().size() == 1) {
                        //delete the store
                        this.deleteStore(pair.getKey());
                    } else {
                        //remove the user from the store owners
                        pair.getKey().removeOwner(toDeleteUserEmail);
                    }
                }
            }
            data.removeUser(toDeleteUserEmail);
            return true;
        }
    }

    @Override
    public Map<Store, Privileges> getStores() throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public Store getStore(String storeName) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public Cart getCart() throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void removeSaleItemFromStore(String currStoreName, String saleType, String type) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void addItemToSale(String storeName, String itemName, String saleType, String startDate, String endDate) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void removeCategoryDiscountFromStore(String storeName, String name, String type, String start, String end) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void addCategoryDiscountToStore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception {
        throw new AdminFunctionException();
    }

    @Override

    public void editCategoryDiscountToScore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception {
        throw new AdminFunctionException();

    }

    @Override
    public Set<Discount> getItemDiscounts(String store, String item) throws Exception {
        throw new AdminFunctionException();
    }

    public void addPurchaseRule(String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail, String storeName, String categoryName, PurchasePolicy purchasePolicyForAdmin) throws Exception {
        if (categoryName != null && itemName != "")
            throw new BadArgumentException("cannot relate to a specific item in the purchase policy of a category");
        purchasePolicyForAdmin.addRule(itemName, notBit, operator, ruleType, numberDetail, stringDetail);
    }

    @Override
    public void removePurchaseRule(String itemName, String storeName, String categoryName, PurchasePolicy purchasePolicyForAdmin) throws Exception {
        purchasePolicyForAdmin.removeRule(itemName);
        purchasePolicyForAdmin.removeLiteralPP(itemName);
    }

    @Override
    public void editDiscountForItem(String itemName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void closeStore(String storeName) throws NoSuchStoreException {
        Store store = data.getStoreByName(storeName);
        store.setIsActive(false);
    }

    @Override
    public Privileges getStorePrivileges(String name) throws Exception {
        throw new AdminFunctionException();
    }

    @Override
    public void editCategoryDiscountToScore(String categoryName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        throw new AdminFunctionException();
    }
}
