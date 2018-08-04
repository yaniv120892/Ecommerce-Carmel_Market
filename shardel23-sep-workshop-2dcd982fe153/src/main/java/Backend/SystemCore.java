package Backend;

import Backend.Addons.EventLogger;
import Backend.Addons.PaymentService.PaymentServiceInterface;
import Backend.Addons.PaymentService.PaymentServiceProxy;
import Backend.Addons.RealTimeAlertsRaffle;
import Backend.Addons.Security;
import Backend.Addons.SupplyService.SupplyServiceInterface;
import Backend.Addons.SupplyService.SupplyServiceProxy;
import Backend.Addons.TreeNode.TreeNode;
import Backend.Addons.Util;
import Backend.Data.DBInterface;
import Backend.Data.ListData;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.PurchasePolicyType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Enums.UserType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Policies.PurchasePolicy;
import Backend.Entities.Privileges;
import Backend.Entities.PurchaseHistory;
import Backend.Entities.Store;
import Backend.Entities.Users.RegUser;
import Backend.Entities.Users.User;
import Exceptions.*;
import javafx.util.Pair;

import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class SystemCore {

    private ConcurrentMap<String, User> usersInSystem;
    private AtomicInteger idCounter = new AtomicInteger(1);
    private DBInterface data;
    private PurchasePolicy systemPurchasePolicy;
    private HashMap<CategoryType, PurchasePolicy> purchasePolicyPerCategory;
    private PaymentServiceInterface paymentService;
    private SupplyServiceInterface supplyService;

    public SystemCore() throws Exception {
        usersInSystem = new ConcurrentHashMap<>();
        paymentService = new PaymentServiceProxy();
        supplyService = new SupplyServiceProxy();

        data = new ListData();
        //data = new HibernateData();

        Thread RTAR = new Thread(new RealTimeAlertsRaffle(data));
        RTAR.start();

        systemPurchasePolicy = new PurchasePolicy(PurchasePolicyType.SYSTEM);
        purchasePolicyPerCategory = new HashMap<>();
        for (CategoryType ct : CategoryType.values())
            purchasePolicyPerCategory.put(ct, new PurchasePolicy(PurchasePolicyType.CATEGORY));
        EventLogger.eventLogger.info("System initialize");

    }

    /**
     * @param userID the userID of the user
     * @return the user with the userID or null if not connected to system
     */
    public User getInSystemUser(String userID) {
        EventLogger.eventLogger.info("Fetching user: "+userID);
        return usersInSystem.get(userID);
    }

    /**
     * @param userID removes the user with @param from the system
     */
    public void removeInSystemUser(String userID) {
        User user = getInSystemUser(userID);
        if(!isInteger(userID) && user != null){
            data.updateUser(user);
        }
        usersInSystem.remove(userID, user);
        EventLogger.eventLogger.info("Removed user: "+userID);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public String getLiteralPurchasePolicy(String itemName, String storeName) throws Exception{
        String LiteralPurchasePolicy;
        String toAdd;
        if(!itemName.equals("")){
            LiteralPurchasePolicy = "When purchasing the item " + itemName + ", a customer must:<br />Admin rules:";
            toAdd = systemPurchasePolicy.getLiteralPP("");
            if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
            toAdd = systemPurchasePolicy.getLiteralPP(itemName);
            if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";

            if(storeName != null && !storeName.equals("")){
                Item item = getItemFromStore(itemName, storeName);
                toAdd = purchasePolicyPerCategory.get(CategoryType.valueOf(item.getCategory().toString())).getLiteralPP("");
                if (!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
                LiteralPurchasePolicy = LiteralPurchasePolicy + "<br />My rules:";
                toAdd = getStoreByName(storeName).getStorePurchasePolicy().getLiteralPP("");
                if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
                toAdd = getStoreByName(storeName).getStorePurchasePolicy().getLiteralPP(itemName);
                if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
                toAdd = getStoreByName(storeName).getCategoryPurchasePolicy(item.getCategory().toString()).getLiteralPP("");
                if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
            }
        }
        else {
            LiteralPurchasePolicy = "When purchasing an item, a customer must:<br />Admin rules:";
            toAdd = systemPurchasePolicy.getLiteralPP("");
            if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
            if(storeName != null && !storeName.equals("")){
                LiteralPurchasePolicy = LiteralPurchasePolicy + "<br />My rules:";
                toAdd = getStoreByName(storeName).getStorePurchasePolicy().getLiteralPP("");
                if(!toAdd.equals("")) LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
            }
        }
        if(storeName != null && !storeName.equals("")){
            if(LiteralPurchasePolicy.equals("When purchasing the item " + itemName + ", a customer must:<br />Admin rules:<br />My rules:"))
                throw new NotPremittedException("the item " + itemName + " doesn't have any purchase rule");
            if(LiteralPurchasePolicy.equals("When purchasing an item, a customer must:<br />Admin rules:<br />My rules:"))
                throw new NotPremittedException("the group All items doesn't have any purchase rule");
        }
        else{
            if(LiteralPurchasePolicy.equals("When purchasing the item " + itemName + ", a customer must:<br />Admin rules:"))
                throw new NotPremittedException("the item " + itemName + " doesn't have any purchase rule");
            if(LiteralPurchasePolicy.equals("When purchasing an item, a customer must:<br />Admin rules:"))
                throw new NotPremittedException("the group All items doesn't have any purchase rule");
        }
        return LiteralPurchasePolicy;
    }

    public String getLiteralPPofCategory(String categoryName, String storeName) throws Exception{
        String LiteralPurchasePolicy;
        if(storeName == null || storeName.equals("")){
            LiteralPurchasePolicy = "When purchasing items from the category " + categoryName + ", a customer must:";
            String toAdd = purchasePolicyPerCategory.get(CategoryType.valueOf(categoryName)).getLiteralPP("");
            if (toAdd.equals("")) {
                throw new NotPremittedException("the category " + categoryName + " doesn't have any purchase rule");
            }
            LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
        }
        else{
            LiteralPurchasePolicy = "When purchasing items from the category " + categoryName +", in the store " + storeName + ", a customer must:";
            String toAdd = getStoreByName(storeName).getCategoryPurchasePolicy(categoryName).getLiteralPP("");
            if (toAdd.equals("")) {
                throw new NotPremittedException("the category " + categoryName + " doesn't have any purchase rule");
            }
            LiteralPurchasePolicy = LiteralPurchasePolicy + toAdd + "<br />";
        }
        return LiteralPurchasePolicy;
    }
    //REQ 1.1

    /**
     * @return new ID for a new user
     */
    public String firstConnection() {
        int id = idCounter.getAndIncrement();
        usersInSystem.put(id + "", new RegUser());
        return id + "";
    }


    //REQ 1.2

    /**
     * @param email
     * @param password
     * @throws NotPremittedException
     */
    public void makeRegister(String email, String password) throws NotPremittedException, BadPasswordException, BadEmailException {
        if (!data.isUserExists(email)) {
            User user = new RegUser();
            //no such user logic - good to go
            String hashedPW = Security.sha256string(password);
            Util.validatePassword(password);
            Util.validateEmailAddress(email);
            user.setType(UserType.REGISTERED);
            user.setEmail(email);
            user.setPassword(hashedPW);
            data.addUser(user);
            usersInSystem.put(email, user);
            EventLogger.eventLogger.info("Registration: "+email);
        } else {
            throw new NotPremittedException("EMAIL ALREADY EXIST");
        }
    }


    //REQ: 1.3

    /**
     * @return all the stores in the system
     */
    public List<Store> getAllStores() {
        List<Store> dataStores = data.getAllStores();
        List<Store> allActiveStores = new ArrayList<>();
        for (Store store : dataStores) {
            if (store.getIsActive()) {
                allActiveStores.add(store);
            }
        }
        EventLogger.eventLogger.info("Fetched all stores");
        return allActiveStores;
    }

    /**
     * @param storeName the name of the store
     * @return store with storename
     */
    public Store getStoreByName(String storeName) {
        for (Store store : data.getAllStores()) {
            if (store.getName().equals(storeName))
                return store;
        }
        EventLogger.eventLogger.info("Fetched store: "+storeName);
        return null;
    }

    /**
     * @param storeName name of the store
     * @return the items in that store
     */
    public ArrayList<Item> getStoresItems(String storeName) {
        for (Store store : data.getAllStores()) {
            if (store.getName().equals(storeName))
                return store.getStoreItems();
        }
        EventLogger.eventLogger.info("Fetching from store "+storeName+" items");
        return new ArrayList<>();
    }

    /**
     * @param storeName name of the store
     * @return the sale items in that store
     */
    public Set<SaleItem> getStoreSaleItems(String storeName) {
        for (Store store : data.getAllStores()) {
            if (store.getName().equals(storeName))
                return store.getStoreSaleItems();
        }
        EventLogger.eventLogger.info("Fetching from store "+storeName+" items for sale");
        return new HashSet<>();
    }

    /**
     * @param storeName name of the store
     * @param category  category of items
     * @return items in that category
     */
    public ArrayList<Item> getStoresItemsInCategory(String storeName, String category) {
        for (Store store : data.getAllStores()) {
            if (store.getName().equals(storeName))
                return store.getStoreItemsInCategory(category);
            //return store.getStoreItemsInCategory(category);
        }
        EventLogger.eventLogger.info("Fetching from store "+storeName+" items in category "+ category);
        return new ArrayList<>();
    }

    /**
     * @param itemName  name of the item
     * @param storeName name of the store
     * @return the item from the store or Null
     */
    public Item getItemFromStore(String itemName, String storeName) {
        for (Store store : data.getAllStores()) {
            if (store.getName().equals(storeName))
                return store.getStoreItem(itemName);
        }
        EventLogger.eventLogger.info("Fetching from store "+storeName+" item "+itemName);
        return null;
    }


    //REQ: 1.5

    /**
     * @param userID    user if of requesting user
     * @param storeName name of the store
     * @param itemName  name of the item
     * @param amount    amount we want to add
     * @param saleType  the saletype which the item is in
     * @throws Exception if failed
     */
    public void addToCart(String userID, String storeName, String itemName, int amount, String saleType) throws Exception {
        User user = getInSystemUser(userID);
        Store store = data.getStoreByName(storeName);
        //get the item from store.
        Item foundItem = store.getStoreItem(itemName);
        if (foundItem == null) {
            throw new NoSuchItemException("ITEM " + itemName + "DOES'NT EXISTS IN STORE " + storeName);
        }
        user.addToCart(foundItem, amount, saleType);
        EventLogger.eventLogger.info("Added to user ,"+userID+", the item "+itemName+" with amount "+amount+" to his cart");
    }


    //REQ: 1.6

    /**
     * @param userID user id requesting
     * @return all the items in the user cart
     * @throws Exception if failed
     */
    public Set<CartItem> getAllItemsFromCart(String userID) throws Exception {
        User user = getInSystemUser(userID);
        EventLogger.eventLogger.info("Fetched the cart of "+userID);
        return user.getMyCartItems();
    }

    /**
     * @param userID    userif requesting
     * @param itemName  name of the item
     * @param storeName name of the store
     * @throws Exception if failed
     */
    public void removeFromCart(String userID, String itemName, String storeName, String saleType) throws Exception {
        User user = getInSystemUser(userID);
        user.removeFromCart(itemName, storeName, saleType);
        EventLogger.eventLogger.info("Removed from the cart of user "+userID+ " "+itemName);
    }

    /**
     * @param userID    user id
     * @param itemName  name of item name of item
     * @param storeName name of store
     * @param newAmount new amount of the item
     * @throws Exception if failed
     */
    public void changeCartItemAmount(String userID, String itemName, String storeName, int newAmount) throws Exception {
        User user = getInSystemUser(userID);
        user.changeCartItemAmount(itemName, storeName, newAmount, SaleType.NORMAL);
        EventLogger.eventLogger.info("Changed the amount of "+itemName+" in the cart of "+userID+ " to "+newAmount);
    }

    /**
     * @param userID     user id
     * @param itemName   name of item name of item
     * @param storeName  name of store
     * @param newPercent new percent of the item
     * @throws Exception if failed
     */
    public void changeCartItemPercent(String userID, String itemName, String storeName, int newPercent) throws Exception {
        User user = getInSystemUser(userID);
        user.changeCartItemAmount(itemName, storeName, newPercent, SaleType.RAFFLE);
        EventLogger.eventLogger.info("Changed the percent of "+itemName+" in the cart of "+userID+ " to "+newPercent);
    }


    //REQ: 1.7

    /**
     * @param userID requesting user
     */
    public Set<Pair<String, String>> checkout(String userID, String creditNumber, int yearValidity, int monthValidity, long cvvCode, String name, String id, String address, String nationalities) throws Exception {
        User user = getInSystemUser(userID);
        Pair<Double, Set<Pair<String, String>>> pair = user.checkout(userID, systemPurchasePolicy, purchasePolicyPerCategory,address);
        if (paymentService.makePayment(pair.getKey(), creditNumber) && supplyService.makeOrder()) {
            Set<Store> storesToUpdate = new HashSet<>();
            for (CartItem cartItem : user.getCart().getItems()) {
                storesToUpdate.add(cartItem.getItem().getStore());
            }
            for (Store store : storesToUpdate) {
                data.updateStore(store);
            }
            user.clearCart();
            EventLogger.eventLogger.info("Checkout worked for user "+userID);
            return pair.getValue();
        }
        user.rollbackCart(user.getCart());
        return null;
    }


    //REQ: 2.1

    /**
     * @param userID   requesting user
     * @param email    email of the person
     * @param password password of the person
     * @throws Exception if failed
     */
    public void makeLogin(String userID, String email, String password) throws Exception {
        User user = getInSystemUser(userID);
        if (user != null && user.getType() == UserType.GUEST) {
            //check email and password
            //if all good, change the user to be the user from db
            String hashedPW = Security.sha256string(password);
            User userFromDB = data.checkUserPassword(email, hashedPW);
            usersInSystem.remove(userID);
            usersInSystem.put(email, userFromDB);
            EventLogger.eventLogger.info("user " + userFromDB.getEmail() + " has logged in");
        } else {
            throw new NotPremittedException("USER IS NOT GUEST OR NO REQUESTING EMAIL IS NOT IN THE SYSTEM");
        }
    }


    //REQ 2.2

    /**
     * /**
     *
     * @param userID       requesting user
     * @param newStoreName new store name
     * @param storeEmail   new store email
     * @throws Exception                      if failed
     * @throws StoreNameAlreadyTakenException store name already taken
     * @throws NotPremittedException          no privileges
     */
    public void openStore(String userID, String newStoreName, String storeEmail) throws Exception {
        User user = getInSystemUser(userID);
        try {
            data.getStoreByName(newStoreName);
            throw new StoreNameAlreadyTakenException("BAD STORE NAME");
        } catch (NoSuchStoreException e) {
            if (user.getType() == UserType.REGISTERED) {
                Store newStore = new Store(newStoreName, storeEmail);
                user.addStoreAsOwner(newStore);
                newStore.addOwner(null, user);
                data.addStore(newStore);
                EventLogger.eventLogger.info("opened store named "+newStoreName);
            } else {
                throw new NotPremittedException("STORE NAME ALREADY EXISTS OR NO PRIVILEGES");
            }
        }
    }


    //REQ 3.1

    /**
     * @param userID               requesting user
     * @param itemName             name of item
     * @param storeName            name of store
     * @param initialAmountInStock first stock
     * @param price                price of item
     * @param category             the item category
     * @throws Exception if failed
     */
    public void addItemToStore(String userID, String itemName, String storeName, int initialAmountInStock, double price, String category) throws Exception {
        User user = getInSystemUser(userID);
        user.addItemToStore(itemName, storeName, initialAmountInStock, price, category);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added item "+itemName+" to store "+storeName);
    }

    /**
     * @param userID    requesting user
     * @param itemName  name of item
     * @param storeName name of store
     * @throws Exception if failed
     */
    public void removeItemFromStore(String userID, String itemName, String storeName) throws Exception {
        User user = getInSystemUser(userID);
        user.removeItemFromStore(itemName, storeName);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Removed item "+itemName+" from store "+storeName);
    }

    /**
     * @param userID    requesting user
     * @param itemName  name of item
     * @param storeName name of store
     * @param newPrice  new price of the item
     * @throws Exception if failed
     */
    public void editStoreItemPrice(String userID, String itemName, String storeName, double newPrice) throws Exception {
        User user = getInSystemUser(userID);
        user.editStoreItemPrice(itemName, storeName, newPrice);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Edited item "+itemName+" at store "+storeName + " to cost "+newPrice);
    }

    /**
     * @param userID    requesting user
     * @param itemName  name of item
     * @param storeName name of store
     * @param newAmount new amount
     * @throws Exception if failed
     */
    public void editStoreItemStock(String userID, String itemName, String storeName, int newAmount) throws Exception {
        User user = getInSystemUser(userID);
        user.editStoreItemStock(itemName, storeName, newAmount);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Edited item "+itemName+" at store "+storeName + " to the amount  "+newAmount);
    }

    /**
     * @param userID    requesting user
     * @param itemName  name of item
     * @param storeName name of store
     * @param isOnSale  is the item on sale?
     * @throws Exception if failed
     */
    public void editStoreItemIsOnSale(String userID, String itemName, String storeName, boolean isOnSale) throws Exception {
        User user = getInSystemUser(userID);
        user.editStoreItemIsOnSale(itemName, storeName, isOnSale);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Edited item "+itemName+" at store "+storeName + " to be on sale "+isOnSale);
    }

    //REQ 3.2
    public void addPurchaseRule(String userID, String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail, String storeName, String categoryName) throws Exception {

        User user = getInSystemUser(userID);
        PurchasePolicy purchasePolicyForAdmin;
        if (categoryName == null)
            purchasePolicyForAdmin = systemPurchasePolicy;
        else
            purchasePolicyForAdmin = purchasePolicyPerCategory.get(CategoryType.valueOf(categoryName));
        user.addPurchaseRule(itemName, notBit, operator, ruleType, numberDetail, stringDetail, storeName, categoryName, purchasePolicyForAdmin);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added purchase rule");
    }

    public void removePurchaseRule(String userID, String itemName, String storeName, String categoryName) throws Exception {
        User user = getInSystemUser(userID);
        PurchasePolicy purchasePolicyForAdmin;
        if (categoryName == null)
            purchasePolicyForAdmin = systemPurchasePolicy;
        else
            purchasePolicyForAdmin = purchasePolicyPerCategory.get(categoryName);
        user.removePurchaseRule(itemName, storeName, categoryName, purchasePolicyForAdmin);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("removed purchase rule");
    }

    /**
     * @param userID       requesting user
     * @param itemName     name of item
     * @param storeName    name of store
     * @param discountType type of discount
     * @param startDate    state date
     * @param endDate      end date
     * @param percentOff   pecrent off
     * @throws Exception if failed
     */
    public void addItemDiscount(String userID, String itemName, String storeName, String discountType, String startDate, String endDate, String percentOff) throws Exception {
        User user = getInSystemUser(userID);
        user.addItemDiscount(itemName, storeName, discountType, startDate, endDate, percentOff);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added discount to store:\nitemName: "+itemName+"\ndiscountType: "+discountType);
    }

    /**
     * @param userID       requesting user
     * @param itemName     name of item
     * @param discountType type of discount
     * @param startDate    state date
     * @param endDate      end date
     * @throws Exception if failed
     */
    public void removeItemDiscount(String userID, String itemName, String storeName, String discountType, String startDate, String endDate) throws Exception {
        User user = getInSystemUser(userID);
        user.removeItemDiscount(itemName, storeName, discountType, startDate, endDate);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Removed discount from store:\nitemName: "+itemName+"\ndiscountType: "+discountType);
    }


    //REQ 3.3

    /**
     * @param userID        requesting user
     * @param newOwnerEmail new owner email
     * @param storeName     name of store
     * @throws Exception if fialed
     */
    public void addNewStoreOwner(String userID, String newOwnerEmail, String storeName) throws Exception {
        User user = getInSystemUser(userID);
        user.appointNewOwner(storeName, data.getUser(newOwnerEmail));
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added new Store owner "+newOwnerEmail);
    }


    //REQ 3.4

    /**
     * @param userID          requesting user
     * @param newManagerEmail new manager email
     * @param storeName       name of store
     * @param privileges      new privliges
     * @throws Exception if failed
     */
    public void appointManager(String userID, String newManagerEmail, String storeName, boolean[] privileges) throws Exception {
        User user = getInSystemUser(userID);
        user.appointManager(data.getUser(newManagerEmail), storeName, privileges);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added manager to store:\nnewManager: "+newManagerEmail);
    }


    //REQ 3.7

    /**
     * @param userID    requesting user
     * @param storeName name of store
     * @return purchase history for store
     * @throws Exception if failed
     */
    public Set<PurchaseHistory> getStorePurchaseHistory(String userID, String storeName) throws Exception {
        User user = getInSystemUser(userID);
        EventLogger.eventLogger.info("Fetching purchase policy for store "+storeName);
        return user.getPurchaseHistoryOfStore(storeName);
    }

    //REQ 4.1 - the manager can make actions based on the privileges given to him
    //No need for implementation.


    //REQ 5.2

    /**
     * @param userID            requesting user
     * @param toDeleteUserEmail who to delete
     * @throws Exception if failed
     */
    public void removeUserFromSystem(String userID, String toDeleteUserEmail) throws Exception {
        User user = getInSystemUser(userID);
        if (user.removeUserFromSystem(toDeleteUserEmail)) {
            removeInSystemUser(userID);
            EventLogger.eventLogger.info("Removed user from system: "+toDeleteUserEmail);
        }
    }


    //REQ 5.4

    //store is in REQ 3.7

    /**
     * @param userID    requesting user
     * @param UserEmail of who
     * @return the purchse history
     */
    public ArrayList<PurchaseHistory> getUserPurchaseHistory(String userID, String UserEmail) {
        //TODO: NEXT VERSION;
        return null;
    }

    /**
     * clears the user cart
     *
     * @param userID requesting user
     * @throws Exception if failed
     */
    public void clearCart(String userID) throws Exception {
        User user = getInSystemUser(userID);
        user.clearCart();
        EventLogger.eventLogger.info("Cleared cart of "+userID);
    }

    //Return a list with all the user stores, if no stores throw exception

    /**
     * @param userID requesting user
     * @return all of my stores
     * @throws Exception if failed
     */
    public List<Store> getMyStores(String userID) throws Exception {
        User user = getInSystemUser(userID);
        Map<Store, Privileges> userStores = user.getStores();
        List<Store> ans = new ArrayList<>();
        for (Entry<Store, Privileges> userStore : userStores.entrySet()) {
            ans.add(userStore.getKey());
        }
        if (ans.size() == 0) {
            throw new NoSuchStoreException("NO STORES FOR THIS USER");
        }
        EventLogger.eventLogger.info("Fetching user "+userID +" stores");
        return ans;
    }

    /**
     * @return all the of the users registered
     */
    public List<User> getAllUsers() {
        EventLogger.eventLogger.info("Fetching All users in the system");
        return data.getAllUsers();
    }

    /**
     * @param userID     requesting user
     * @param couponCode the code
     * @throws Exception if failed
     */
    public void addCouponToCart(String userID, String couponCode) throws Exception {
        User user = getInSystemUser(userID);
        user.addCoupon(couponCode);
        EventLogger.eventLogger.info("Coupon "+couponCode+" added to user "+userID);
    }

    /**
     * removes coupon code from cart
     *
     * @param userID     reqeusting user
     * @param couponCode the code
     * @throws Exception if failed
     */
    public void removeCouponFromCart(String userID, String couponCode) throws Exception {
        User user = getInSystemUser(userID);
        if (!user.removeCoupon(couponCode)) {
            throw new NotPremittedException("Coupon was not found");
        }
        EventLogger.eventLogger.info("Removed coupon "+couponCode+ " from cart");
    }

    /**
     * @param userID requesting user
     * @return coupons in the users cart
     * @throws Exception if failed
     */
    public Set<String> getCouponsInCart(String userID) throws Exception {
        User user = getInSystemUser(userID);
        EventLogger.eventLogger.info("Fetching coupons in cart");
        return user.getUserCoupons();
    }

    /**
     * removes all the coupons from cart
     *
     * @param userID reqeusting user
     * @throws Exception if failed
     */
    public void removeAllCouponFromCart(String userID) throws Exception {
        User user = getInSystemUser(userID);
        user.deleteAllCoupons();
        EventLogger.eventLogger.info("Removing all coupons in cart");
    }

    /**
     * @param userID    requesting user
     * @param storeName the name of store
     * @throws Exception if failed
     */
    public void closeStore(String userID, String storeName) throws Exception {
        User user = getInSystemUser(userID);
        user.closeStore(storeName);
        EventLogger.eventLogger.info("User "+userID+" closed store "+storeName);
    }

    /**
     * @param itemName  the item name
     * @param storeName the store name
     * @return sale items in store
     * @throws Exception if failed
     */
    public Set<SaleItem> getSaleItemsByItemInStore(String itemName, String storeName) throws Exception {
        EventLogger.eventLogger.info("Fetching all items in sale from store " + storeName);
        return data.getStoreByName(storeName).getStoreSaleItems();
    }

    /**
     * @param storeName the store name
     * @param category  the items category
     * @return all the saleitems in the store with that category
     * @throws Exception if failed
     */
    public Set<SaleItem> getSaleItemsByCategoryInStore(String storeName, String category) throws Exception {
        Set<SaleItem> items = new HashSet<>();
        for (Store store : data.getAllStores()) {
            store.addSaleItemsByCategory(CategoryType.valueOf(category), items);
        }
        EventLogger.eventLogger.info("Fetching items by category "+category+" in sale from store " + storeName);
        return items;
    }

    /**
     * @param itemName item name
     * @return the items with that name
     */
    public ArrayList<SaleItem> getSaleItemsByItemName(String itemName) {
        ArrayList<SaleItem> items = new ArrayList<>();
        for (Store store : data.getAllStores()) {
            store.addSaleItemsByName(itemName, items);
        }
        EventLogger.eventLogger.info("Fetching all sale items " + itemName);
        return items;
    }

    /**
     * @param userID        requesting user
     * @param currItemName  the item name
     * @param currStoreName the store name
     * @param saleType      the sale type
     * @throws Exception if failed
     */
    public void removeSaleItemFromStore(String userID, String currItemName, String currStoreName, String saleType) throws Exception {
        User user = getInSystemUser(userID);
        user.removeSaleItemFromStore(currStoreName, currItemName, saleType);
        Store store = user.getStore(currStoreName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Removed sale item "+currItemName+" from store " + currStoreName);
    }

    /**
     * @param userID    requesting user
     * @param storeName store name
     * @param itemName  item name
     * @param saleType  sale type
     * @throws NoSuchStoreException  if no store with that name
     * @throws NotPremittedException if no privileges
     */
    public void addItemToSale(String userID, String storeName, String itemName, String saleType) throws NoSuchStoreException, NotPremittedException {
        User user = getInSystemUser(userID);
        Store store = data.getStoreByName(storeName);
        store.addItemToSale(itemName, saleType);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added item to sale " + itemName + " in store "+storeName);
    }

    /**
     * @param userID    requesting user
     * @param storeName store name
     * @param itemName  item name
     * @param saleType  sale type
     * @param startDate start date
     * @param endDate   end date
     * @throws Exception if failed
     */
    public void addItemToSale(String userID, String storeName, String itemName, String saleType, String startDate, String endDate) throws Exception {
        User user = getInSystemUser(userID);
        user.addItemToSale(storeName, itemName, saleType, startDate, endDate);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        //data.getStoreByName(storeName).addItemToSale(itemName, saleType, startDate, endDate);
        EventLogger.eventLogger.info("Added item to sale " + itemName + " in store "+storeName);
    }

    /**
     * @return all items
     */
    public List<Item> getAllItems() {
        EventLogger.eventLogger.info("Fetching all items");
        return data.getAllItems();
    }

    /**
     * @return all sale items
     */
    public List<SaleItem> getAllSaleItems() {
        EventLogger.eventLogger.info("Fetching all sale items");
        return data.getAllSaleItems();
    }

    /**
     *
     * @param selectedStoreName the store name
     * @return discount by category
     * @throws ParseException if failed
     */
        /*
    public ArrayList<Pair<CategoryType, Backend.Entities.Discount>> getCategoryDiscountsByStoreName(String selectedStoreName) throws ParseException {
        //TODO Roy to add implementation issue #86
        ArrayList<Pair<CategoryType, Backend.Entities.Discount>> ans = new ArrayList<Pair<CategoryType, Backend.Entities.Discount>>();
        Backend.Entities.Discount d = new VisibleDiscount("12-08-2010", "20-08-2012", 15);
        ans.add(new Pair<CategoryType, Backend.Entities.Discount>(CategoryType.BOOKS, d));
        return ans;
    }
    */

    /**
     * @param userID        requesting user
     * @param categoryName  category name
     * @param currStoreName store name
     * @throws Exception if failed
     */
    public void removeCategoryDiscountFromStore(String userID, String categoryName, String currStoreName) throws Exception {
        //TODO Roy to add implementation issue #91
        throw new NotPremittedException("Not implemented issue #91");
    }

    /**
     * @param userID       requesting user
     * @param storeName    store name
     * @param categoryName category name
     * @param startDate    start date
     * @param endDate      end date
     * @param percent      percent
     * @throws Exception if failed
     */
    public void addCategoryDiscountToStore(String userID, String storeName, String categoryName, String startDate, String endDate, String percent) throws Exception {
        //TODO Roy to add implementation issue #92
        throw new NotPremittedException("Not implemented issue #92");
    }

    public Map<CategoryType, Discounts> getCategoryDiscountsByStoreName(String selectedStoreName) throws ParseException, NoSuchStoreException {
        EventLogger.eventLogger.info("Fetched category discounts from store "+selectedStoreName);
        return data.getCategoryDiscountsByStoreName(selectedStoreName);
    }

    public void removeCategoryDiscountFromStore(String userID, String categoryName, String currStoreName, String type, String start, String end) throws Exception {
        User user = getInSystemUser(userID);
        user.removeCategoryDiscountFromStore(currStoreName, categoryName, type, start, end);
        Store store = user.getStore(currStoreName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Removed category "+categoryName+" discount from store "+currStoreName);
    }

    public void addCategoryDiscountToStore(String userID, String storeName, String categoryName,
                                           String discountType, String startDate, String endDate,
                                           String percent) throws Exception, NoSuchDiscountException {
        User user = getInSystemUser(userID);
        user.addCategoryDiscountToStore(storeName, categoryName, discountType, startDate, endDate, percent);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Added category "+categoryName+" discount to store "+storeName);
    }


    public void editCategoryDiscountToScore(String userID, String categoryName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent
            , String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        User user = getInSystemUser(userID);
        user.editCategoryDiscountToScore(categoryName, storeName, newDiscountType, newStartDate, newEndDate, newPercent , oldDiscountType, oldStartDate, oldEndDate, oldPercent);
        EventLogger.eventLogger.info("Edited category "+categoryName+" discount from store "+storeName);
    }

    public Set<Discount> getItemDiscounts(String userID, String store, String item) throws Exception {
        User user = getInSystemUser(userID);
        EventLogger.eventLogger.info("Fetching item "+item+" discounts");
        return user.getItemDiscounts(store, item);
    }

    public void editDiscountFromItem(String userID, String itemName, String storeName,
                                     String newDiscountType, String newStartDate, String newEndDate,
                                     String newPercent, String oldDiscountType, String oldStartDate,
                                     String oldEndDate, String oldPercent) throws Exception {
        User user = getInSystemUser(userID);
        user.editDiscountForItem(itemName, storeName, newDiscountType, newStartDate, newEndDate, newPercent, oldDiscountType, oldStartDate, oldEndDate, oldPercent);
        Store store = user.getStore(storeName);
        data.updateStore(store);
        EventLogger.eventLogger.info("Editing item "+itemName+" discount");
    }

    public ArrayList<SaleItem> getSaleItemsByCategory(String categoryName) throws Exception {
        ArrayList<SaleItem> items = new ArrayList<>();
        for(Store store : data.getAllStores()){
            if(store.getIsActive()){
                store.getSaleItemsByCategory(categoryName, items);
            }
        }
        EventLogger.eventLogger.info("Fetching sale items by category "+categoryName);
        return items;
    }

    public Iterator<TreeNode<User>> getAppointmentsHistory(String storeName) throws Exception {
        EventLogger.eventLogger.info("Fetching appointments history from store "+storeName);
        return data.getStoreByName(storeName).getTree().iterator();
    }

    public Privileges getUserPrivilages(String userID, String storeName, String userToShow) throws Exception {
        //User user = getInSystemUser(userID);
        User user = data.getUser(userToShow);
        EventLogger.eventLogger.info("Fetching user privilages for "+userToShow);
        return user.getStorePrivileges(storeName);
    }

    public ConcurrentHashMap<String,Integer> getWebsocketUsersStatistics() throws Exception{
        ConcurrentHashMap<String,Integer> ans =  new ConcurrentHashMap<String,Integer>();
        ans.put("Day 1", 100);
        ans.put("Day 2", 200);
        ans.put("Day 3", 300);
        ans.put("Day 4", 400);
        ans.put("Day 5", 500);
        return ans;

        //throw new Exception("Not implemeted yet");
    }

}
