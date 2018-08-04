package Backend.Entities.Users;


import Backend.Addons.EventLogger;
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
import Exceptions.*;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.*;
import java.util.Map.Entry;

@Entity
public class RegUser extends User {

    @OneToOne(cascade = CascadeType.ALL)
    private Cart myCart;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<Store, Privileges> myStores;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<PurchaseHistory> purchaseHistory;

    public RegUser() {
        super();
        this.myCart = new Cart();
        this.myStores = new HashMap<>();
        purchaseHistory = new HashSet<>();
    }

    private ArrayList<String> errorPolicyCheck(Cart cart, String sendingAddress, PurchasePolicy systemPP, HashMap<CategoryType, PurchasePolicy> PpPerCategory) throws Exception {
        ArrayList<String> errorsList = new ArrayList();
        errorsList.addAll(systemPP.validateCart(cart, sendingAddress));
        for(CartItem cartItem : cart.getItems()){
            String itemName = cartItem.getItem().getName();
            PurchasePolicy categoryPolicy = PpPerCategory.get(cartItem.getItem().getCategory());
            PurchasePolicy storePolicy = cartItem.getItem().getStore().getStorePurchasePolicy();
            PurchasePolicy categoryInStorePolicy = cartItem.getItem().getStore().getCategoryPurchasePolicy(cartItem.getItem().getCategory().toString());
            errorsList.addAll(categoryPolicy.validateItem(itemName, cartItem.getAmount(), sendingAddress));
            errorsList.addAll(storePolicy.validateItem(itemName, cartItem.getAmount(), sendingAddress));
            errorsList.addAll(categoryInStorePolicy.validateItem(itemName, cartItem.getAmount(), sendingAddress));
        }
        return errorsList;
    }

    @Override
    public Pair<Double, Set<Pair<String, String>>> checkout(String userID, PurchasePolicy systemPP, HashMap<CategoryType, PurchasePolicy> PpPerCategory, String address) throws Exception {
        ArrayList<String> errorList = errorPolicyCheck(myCart, address, systemPP, PpPerCategory);
        if (errorList.size() != 0) throw new PurchasePolicyException(errorList);
        Cart futureRollback = new Cart();
        double total = 0;
        Set<Pair<String, String>> messages = new HashSet<>();
        for (CartItem item : myCart.getItems()) {
            try {
                total += item.getItem().checkout(this, item.getAmount(), item.getSaleType(), messages);
                /*for (User user : item.getItem().getStore().getOwners()) {
                    messages.add(new Pair<>(user.getEmail(), "Someone has bought" + item.getItem().getName() + " from you"));
                    /*
                total += item.getItem()().checkout(this, item.getAmount()(), item.getSaleType()());
                for (User user : item.getItem()().getStore().getOwners()) {
                    owners.add(user.getEmail());

                }*/
                futureRollback.addItem(item);
                purchaseHistory.add(new PurchaseHistory(item.getItem().getName(), userID, item.getItem().getStoreName(), item.getAmount(), item.getItem().getPrice(), "desc"));
            } catch (CheckoutFailedException e) {
                rollbackCart(futureRollback);
                for (int i = 0; i < futureRollback.getItems().size(); i++) {
                    purchaseHistory.remove(purchaseHistory.size() - 1);
                }
                throw new CheckoutFailedException(e.getMessage());
            }
        }
        return new Pair<>(total, messages);
    }

    public void rollbackCart(Cart futureRollback) throws Exception {
        for (CartItem item : futureRollback.getItems()) {
            item.getItem().rollback(this, item.getAmount(), item.getSaleType());
        }
    }

    @Override
    public void clearCart() {
        this.myCart = new Cart();
    }

    @Override
    public Set<PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    @Override
    public void deleteStore(Store store) throws NotPremittedException {
        throw new NotPremittedException("YOU ARE NOT AN ADMIN!");
    }

    public boolean isOwnerOrManager(String storeName) {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserType getType() {
        return super.getType();
    }

    @Override
    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public void addToCart(Item storeItem, int amount, String saleType) {
        myCart.addItem(storeItem, amount, saleType);
    }

    @Override
    public Set<CartItem> getMyCartItems() {
        return myCart.getItems();
    }

    @Override
    public void removeFromCart(String itemName, String storeName, String saleType) throws Exception {
        myCart.removeItem(itemName, storeName, saleType);
    }

    @Override
    public void changeCartItemAmount(String itemName, String storeName, int newAmount, SaleType saletype) throws Exception {
        if (saletype.equals(SaleType.RAFFLE) && newAmount > 100) {
            throw new NotPremittedException("PERCENT CANNOT BE MORE THAN 100!");
        }
        myCart.changeCartItemAmount(itemName, storeName, newAmount, saletype);
    }

    @Override
    public void addStoreAsOwner(Store newStore) {
        myStores.put(newStore, new Privileges(true));
    }

    @Override
    public void addItemToStore(String itemName, String storeName, int initialAmountInStock, double price, String category) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canAddItemToStore()) {
                pair.getKey().addItemToStore(itemName, initialAmountInStock, price, category);
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void removeItemFromStore(String itemName, String storeName) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canRemoveItemFromStore()) {
                {
                    pair.getKey().removeItemFromStore(itemName);
                    return;
                }
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void editStoreItemPrice(String itemName, String storeName, double newPrice) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canEditItemPrice()) {
                pair.getKey().editItemPrice(itemName, newPrice);
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void editStoreItemStock(String itemName, String storeName, int newAmount) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canEditItemStock()) {
                pair.getKey().editItemStock(itemName, newAmount);
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void editStoreItemIsOnSale(String itemName, String storeName, boolean newValue) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canEditItemIsOnSale()) {
                pair.getKey().editItemIsOnSale(itemName, newValue);
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void addItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate, String percentOff) throws Exception {
        if (Integer.parseInt(percentOff) > 100) {
            throw new NotPremittedException("PERCENT CANNOT BE GREATER THAN 100!");
        }
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canAddItemDiscount()) {
                pair.getKey().addItemDiscount(itemName, discountType, startDate, endDate, Double.parseDouble(percentOff));
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void removeItemDiscount(String itemName, String storeName, String discountType, String startDate, String endDate) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canRemoveItemDiscount()) {
                pair.getKey().removeItemDiscount(itemName, discountType, startDate, endDate);
                return;
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public void appointNewOwner(String storeName, User newOwner) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName)) {
                if (pair.getValue().canAddOwner()) {
                    if (newOwner.getType() == UserType.REGISTERED) {
                        if (!newOwner.isOwnerOrManager(storeName)) {
                            newOwner.getStores().put(pair.getKey(), new Privileges(true));
                            pair.getKey().addOwner(this, newOwner);
                            return;
                        }
                        throw new NotPremittedException("USER ALREADY IS A MANAGER OR OWNER");
                    }
                    throw new NotPremittedException("USER IS NOT REGISTERED");
                }
                throw new NotPremittedException("NO PRIVILEGES!");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES");
    }

    @Override
    public void appointManager(User newManager, String storeName, boolean[] privileges) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName)) {
                if (pair.getValue().canAddManager()) {
                    if (newManager.getType() == UserType.REGISTERED) {
                        if (!newManager.isOwnerOrManager(storeName)) {
                            newManager.getStores().put(pair.getKey(), new Privileges(privileges));
                            pair.getKey().addManager(this, newManager);
                            return;
                        }
                        throw new NotPremittedException("USER ALREADY IS A MANAGER OR OWNER");
                    }
                    throw new NotPremittedException("USER IS NOT REGISTERED");
                }
                throw new NotPremittedException("NO PRIVILEGES!");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES");
    }

    @Override
    public Set<PurchaseHistory> getPurchaseHistoryOfStore(String storeName) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName) && pair.getValue().canGetPurchaseHistory()) {
                return super.getStorePurchaseHistory(pair.getKey());
            }
        }
        throw new NotPremittedException("NO PRIVILEGES OR NO SUCH STORE IN MY STORES");
    }

    @Override
    public boolean removeUserFromSystem(String toDeleteUserEmail) throws Exception {
        throw new NotPremittedException("Non-Admin cannot remove from system!");
    }

    @Override
    public Map<Store, Privileges> getStores() {
        return myStores;
    }

    @Override
    public Store getStore(String storeName) throws Exception {
        for (Store store :  myStores.keySet()) {
            if (store.getName().equals(storeName)) {
                return store;
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE: " + storeName);
    }

    @Override
    public Cart getCart() {
        return this.myCart;
    }

    @Override
    public void removeSaleItemFromStore(String currStoreName, String itemName, String saleType) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(currStoreName)) {
                if (pair.getValue().canRemoveSaleItemFromStore()) {
                    pair.getKey().removeSaleItemFromStore(itemName, saleType);
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES");
    }

    @Override
    public void addItemToSale(String storeName, String itemName, String saleType, String startDate, String endDate) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName)) {
                if (pair.getValue().canRemoveSaleItemFromStore()) {
                    pair.getKey().addItemToSale(itemName, saleType, startDate, endDate);
                    return;
                }
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES");
    }

    public void removeCategoryDiscountFromStore(String storeName, String categoryName, String discountType, String start, String end) throws Exception {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(storeName)) {
                if (store.getValue().canRemoveCategoryDiscount()) {
                    store.getKey().removeCategoryDiscount(categoryName, discountType, start, end);
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    @Override
    public void addCategoryDiscountToStore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception, NoSuchDiscountException {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(storeName)) {
                if (store.getValue().canRemoveCategoryDiscount()) {
                    store.getKey().addCategoryDiscount(categoryName, discountType, startDate, endDate, Double.parseDouble(percent));
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    @Override
    public void editCategoryDiscountToScore(String storeName, String categoryName, String discountType, String startDate, String endDate, String percent) throws Exception {
        throw new NotPremittedException("NOT IMPLEMENTED");
    }

    @Override
    public Set<Discount> getItemDiscounts(String store, String item) throws Exception {
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(store)) {
                return pair.getKey().getItemDiscounts(item);
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE");
    }

    public PurchasePolicy getRightPurchasePolicy(String storeName, String categoryName) {
        PurchasePolicy pp = null;
        for (Entry<Store, Privileges> pair : myStores.entrySet()) {
            if (pair.getKey().getName().equals(storeName))
                if (categoryName != null)
                    pp = pair.getKey().getCategoryPurchasePolicy(categoryName);
                else
                    pp = pair.getKey().getStorePurchasePolicy();
        }
        return pp;
    }

    @Override
    public void addPurchaseRule(String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail, String storeName, String categoryName, PurchasePolicy PurchasePolicyForAdmin)throws Exception{
        if(categoryName != null && !itemName.equals(""))
            throw new BadArgumentException("cannot relate to a specific item in the purchase policy of a category");

        PurchasePolicy pp = getRightPurchasePolicy(storeName, categoryName);
        if(pp != null)
            pp.addRule(itemName, notBit, operator, ruleType, numberDetail, stringDetail);
        else
            throw new BadArgumentException("storeName, the user is probably not the owner of " + storeName);
    }

    @Override
    public void removePurchaseRule(String itemName, String storeName, String categoryName, PurchasePolicy purchasePolicyForAdmin) throws Exception {
        PurchasePolicy pp = getRightPurchasePolicy(storeName, categoryName);
        if (pp != null){
            pp.removeRule(itemName);
            pp.removeLiteralPP(itemName);
        }
        else
            throw new BadArgumentException("storeName, the user is probably not the owner of " + storeName);
    }

    @Override
    public void editDiscountForItem(String itemName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(storeName)) {
                if (store.getValue().canEditDiscountForItem()) {
                    store.getKey().editDiscountForItem(itemName, newDiscountType, newStartDate, newEndDate, newPercent, oldDiscountType, oldStartDate, oldEndDate, oldPercent);
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    @Override
    public void closeStore(String storeName) throws NoSuchStoreException, NotPremittedException {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(storeName)) {
                if (store.getValue().canCloseStore()) {
                    store.getKey().setIsActive(false);
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    @Override
    public Privileges getStorePrivileges(String name) throws Exception {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(name)) {
                return store.getValue();
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    @Override
    public void editCategoryDiscountToScore
            (String categoryName, String storeName, String newDiscountType,
             String newStartDate, String newEndDate, String newPercent, String oldDiscountType,
             String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        for (Entry<Store, Privileges> store : myStores.entrySet()) {
            if (store.getKey().getName().equals(storeName)) {
                if (store.getValue().canEditCategoryDiscountForItem()) {
                    store.getKey().editCategoryDiscount(categoryName, newDiscountType, newStartDate, newEndDate, newPercent, oldDiscountType, oldStartDate, oldEndDate, oldPercent);
                    return;
                }
                throw new NotPremittedException("NO PRIVILEGES");
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE IN MY STORES!");
    }

    public CartItem getCartItem(String itemName, String storeName) {
        for(CartItem cartItem : myCart.getItems()){
            Item item = cartItem.getItem();
            if (item.getName().equals(itemName) && item.getStore().getName().equals(storeName)) {
                return cartItem;
            }
        }
        return null;

    }


}
