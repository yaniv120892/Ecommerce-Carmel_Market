package ServiceLayer;

import Backend.Addons.EventLogger;
import Backend.Addons.TreeNode.TreeNode;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Privileges;
import Backend.Entities.PurchaseHistory;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import Backend.SystemCore;
import Exceptions.NoSuchStoreException;
import Exceptions.NotPremittedException;
import WebSocket.Publisher;
import WebSocket.Stats;
import javafx.util.Pair;

import javax.websocket.Session;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProvider {

    private SystemCore sc;
    private Publisher publisher = Publisher.getPublisher();
    private Stats stats;

    private static ServiceProvider sp;

    public static ServiceProvider getSP() {
        if (sp == null) {
            try {
                sp = new ServiceProvider();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sp;
    }

    private ServiceProvider() throws Exception {
        sc = new SystemCore();
        stats = Stats.getStats();
    }

    public User getInSystemUser(String userID) {
        return sc.getInSystemUser(userID);
    }

    public void removeInSystemUser(String userID) {
        sc.removeInSystemUser(userID);
    }

    //REQ 1.1
    public String firstConnection() {
        String ans = sc.firstConnection();
        stats.firstConnection();
        return ans;
    }

    //REQ 1.2
    public void makeRegister(String email, String password) throws Exception {
        sc.makeRegister(email, password);
    }

    //REQ: 1.3
    public List<Store> getAllStores() {
        return sc.getAllStores();
    }

    public List<Store> getMyStores(String userID) throws Exception {
        return sc.getMyStores(userID);
    }

    //Store name is unique
    public Store getStoreByName(String storeName) {
        return sc.getStoreByName(storeName);
    }

    public ArrayList<Item> getStoresItems(String storeName) {
        return sc.getStoresItems(storeName);
    }

    public Set<SaleItem> getStoresSaleItems(String storeName) {
        return sc.getStoreSaleItems(storeName);
    }

    public Set<SaleItem> getStoresItemsInCategory(String storeName, String category) throws Exception {
        //TODO Omry to add implementation issue #73
        return sc.getSaleItemsByCategoryInStore(storeName, category);
    }

    public ArrayList<SaleItem> getSaleItemsByItemName(String itemName) {
        return sc.getSaleItemsByItemName(itemName);
    }

    public Item getItemFromStore(String itemName, String storeName) {
        return sc.getItemFromStore(itemName, storeName);
    }


    //REQ: 1.5
    public void addToCart(String userID, String storeName, String itemName, int amount, String saleType) throws Exception {
        sc.addToCart(userID, storeName, itemName, amount, saleType);
    }


    //REQ: 1.6
    public Set<CartItem> getAllItemsFromCart(String userID) throws Exception {
        return sc.getAllItemsFromCart(userID);
    }

    public void removeFromCart(String userID, String itemName, String storeName, String saleType) throws Exception {
        sc.removeFromCart(userID, itemName, storeName, saleType);
    }

    public void changeCartItemAmount(String userID, String itemName, String storeName, int newAmount) throws Exception {
        sc.changeCartItemAmount(userID, itemName, storeName, newAmount);
    }

    public void changeCartItemPercent(String userID, String itemName, String storeName, int newPercent) throws Exception {
        sc.changeCartItemPercent(userID, itemName, storeName, newPercent);
    }

    //REQ: 1.7
    public void checkout(String userID, String creditNumber, int yearValidity, int monthValidity, long cvvCode, String name, String id, String address, String nationalities) throws Exception {
        Set<Pair<String, String>> set = sc.checkout(userID, creditNumber, yearValidity, monthValidity, cvvCode, name, id, address, nationalities);
        for (Pair<String, String> pair : set) {
            publisher.sendToUser(pair.getKey(), pair.getValue());
        }
    }


    //REQ: 2.1
    public void makeLogin(String userID, String email, String password) throws Exception {
        sc.makeLogin(userID, email, password);
        stats.makeLogin(email);
    }


    //REQ 2.2
    public void openStore(String userID, String newStoreName, String storeEmail) throws Exception {
        sc.openStore(userID, newStoreName, storeEmail);
    }


    //REQ 3.1

    public void addItemToStore(String userID, String itemName, String storeName, int initialAmountInStock, double price, String category) throws Exception {
        sc.addItemToStore(userID, itemName, storeName, initialAmountInStock, price, category);
    }

    public void removeItemFromStore(String userID, String itemName, String storeName) throws Exception {
        sc.removeItemFromStore(userID, itemName, storeName);
    }

    public void editStoreItemPrice(String userID, String itemName, String storeName, double newPrice) throws Exception {
        sc.editStoreItemPrice(userID, itemName, storeName, newPrice);
    }

    public void editStoreItemStock(String userID, String itemName, String storeName, int newAmount) throws Exception {
        sc.editStoreItemStock(userID, itemName, storeName, newAmount);
    }

    public void editStoreItemIsOnSale(String userID, String itemName, String storeName, boolean isOnSale) throws Exception {
        sc.editStoreItemIsOnSale(userID, itemName, storeName, isOnSale);
    }

    //REQ 3.2
    public void addItemDiscount(String userID, String itemName, String storeName, String discountType, String startDate, String endDate, String percentOff) throws Exception {
        sc.addItemDiscount(userID, itemName, storeName, discountType, startDate, endDate, percentOff);
    }

    public void removeItemDiscount(String userID, String itemName, String storeName, String discountType, String startDate, String endDate) throws Exception {
        sc.removeItemDiscount(userID, itemName, storeName, discountType, startDate, endDate);
    }


    //REQ 3.3
    public void addNewStoreOwner(String userID, String newOwnerEmail, String storeName) throws Exception {
        sc.addNewStoreOwner(userID, newOwnerEmail, storeName);
    }


    //REQ 3.4
    public void appointManager(String userID, String newManagerEmail, String storeName, boolean[] privileges) throws Exception {
        sc.appointManager(userID, newManagerEmail, storeName, privileges);
    }


    //REQ 3.7
    public Set<PurchaseHistory> getStorePurchaseHistory(String userID, String storeName) throws Exception {
        return sc.getStorePurchaseHistory(userID, storeName);
    }


    //REQ 4.1 - the manager can make actions based on the privileges given to him
    //No need for implementation.


    //REQ 5.2
    public void removeUserFromSystem(String userID, String toDeleteUserEmail) throws Exception {
        sc.removeUserFromSystem(userID, toDeleteUserEmail);
    }


    //REQ 5.4
    public ArrayList<PurchaseHistory> getUserPurchaseHistory(String userID, String UserEmail) {
        return sc.getUserPurchaseHistory(userID, UserEmail);
    }

    //REQ 6
    public void addPurchaseRule(String userID, String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail, String storeName, String categoryName) throws Exception {
        sc.addPurchaseRule(userID, itemName, notBit, operator, ruleType, numberDetail, stringDetail, storeName, categoryName);
    }

    public void removePurchaseRule(String userID, String itemName, String storeName, String categoryName) throws Exception {
        sc.removePurchaseRule(userID, itemName, storeName, categoryName);
    }

    //-----------------------------------------------------------------

    public void makeLogout(String userID) {
        sc.removeInSystemUser(userID);
    }

    public void clearCart(String userID) throws Exception {
        sc.clearCart(userID);
    }


    public List<User> getAllUsers() {
        return sc.getAllUsers();
    }

    public void sendNotification(String msg) throws IOException {
        publisher.sendAll(msg);
    }


    //8.3
    public void addCouponToCart(String userID, String couponCode) throws Exception {
        sc.addCouponToCart(userID, couponCode);
    }

    public void removeCouponFromCart(String userID, String couponCode) throws Exception {
        sc.removeCouponFromCart(userID, couponCode);
    }

    public Set<String> getCouponsInCart(String userID) throws Exception {
        return sc.getCouponsInCart(userID);
    }

    public void addItemToSale(String userID, String storeName, String itemName, String saleType) throws NoSuchStoreException, NotPremittedException {
        sc.addItemToSale(userID, storeName, itemName, saleType);
    }

    public void addItemToSale(String userID, String storeName, String itemName, String saleType, String startDate, String endDate) throws Exception {
        sc.addItemToSale(userID, storeName, itemName, saleType, startDate, endDate);
    }

    public void closeStore(String userID, String storeName) throws Exception {
        sc.closeStore(userID, storeName);
    }

    public Set<SaleItem> getSaleItemsByItemInStore(String itemName, String storeName) throws Exception {
        return sc.getSaleItemsByItemInStore(itemName, storeName);
    }

    public void removeSaleItemFromStore(String userID, String currItemName, String currStoreName, String saleType) throws Exception {
        sc.removeSaleItemFromStore(userID, currItemName, currStoreName, saleType);
    }

    public List getAllItems() {
        return sc.getAllItems();
    }

    public List getAllSaleItems() {
        return sc.getAllSaleItems();
    }

    public Map<CategoryType, Discounts> getCategoryDiscountsByStoreName(String selectedStoreName) throws ParseException, NoSuchStoreException {
        return sc.getCategoryDiscountsByStoreName(selectedStoreName);
    }

    public void removeCategoryDiscountFromStore(String userID, String categoryName, String currStoreName, String type, String start, String end) throws Exception {
        sc.removeCategoryDiscountFromStore(userID, categoryName, currStoreName, type, start, end);
    }

    public void addCategoryDiscountToStore(String userID, String storeName, String categoryName,
                                           String discountType, String startDate, String endDate,
                                           String percent) throws Exception {
        sc.addCategoryDiscountToStore(userID, storeName, categoryName, discountType,
                startDate, endDate, percent);
    }

    public void editCategoryDiscountToScore(String userID, String categoryName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent
            , String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        sc.editCategoryDiscountToScore(userID, categoryName, storeName, newDiscountType, newStartDate, newEndDate, newPercent
                , oldDiscountType, oldStartDate, oldEndDate, oldPercent);
    }

    public Set<Discount> getItemDiscounts(String userID, String store, String item) throws Exception {
        return sc.getItemDiscounts(userID, store,item);
    }

    public Map<String, Session> getWebsocketUsers() {
        return publisher.getUsers();
    }

    public void sendNotification(String userID, String msg) throws IOException {
        publisher.sendToUser(userID, msg);
    }

    public void editDiscountFromItem(String userID, String itemName, String storeName, String newDiscountType, String newStartDate, String newEndDate, String newPercent
            , String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent) throws Exception {
        sc.editDiscountFromItem(userID, itemName, storeName, newDiscountType, newStartDate, newEndDate, newPercent, oldDiscountType, oldStartDate, oldEndDate, oldPercent);


    }

    public ArrayList<SaleItem> getSaleItemsByCategory(String categoryName) throws Exception {
        return sc.getSaleItemsByCategory(categoryName);
    }

    public Iterator<TreeNode<User>> getAppointmentsHistory(String storeName) throws Exception {
        return sc.getAppointmentsHistory(storeName);
    }

    public Privileges getUserPrivilages(String userID, String storeName, String userToShow) throws Exception {
        return sc.getUserPrivilages(userID, storeName, userToShow);
    }

    public String getLiteralPurchaseRule(String itemName, String storeName, String categoryName) throws Exception {
        if (categoryName == null)
            return sc.getLiteralPurchasePolicy(itemName, storeName);
        else
            return sc.getLiteralPPofCategory(categoryName, storeName);
    }

    public ConcurrentHashMap<String,Integer> getWebsocketUsersStatistics() throws Exception {
        return  sc.getWebsocketUsersStatistics();
    }
}





