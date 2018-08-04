package ServiceLayer;

import Backend.Addons.TreeNode.TreeNode;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.*;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ServiceProviderTest {

    private ServiceProvider sp;
    private static boolean setUpIsDone = false;

    private static String storeName;
    private static String itemName;
    private static String itemCategory;
    private static String firstUserId;
    private static String saleTypeNormal;
    private static String storeEmail;
    private static String firstUserEmail;
    private static String firstUserPassword;
    private static String secondUserId;
    private static String secondUserEmail;
    private static String secondUserPassword;
    private static String secondStoreName;
    private static String secondStoreEmail;
    private static double firstItemPrice;
    private static int firstItemStock;
    private static NationalityBackup nationality;

    private static final int NUM_OF_EXTRA_USERS = 10;
    private static String[] usersEmails = new String[NUM_OF_EXTRA_USERS];


    public ServiceProviderTest() {
        sp = ServiceProvider.getSP();
    }

    private void populateSystemData() {
        try {
            saleTypeNormal = "NORMAL";
            firstUserId = sp.firstConnection();
            firstUserEmail = "user@gmail.com";
            firstUserPassword = "12345678";
            nationality = NationalityBackup.UNITED_STATES;
            sp.makeRegister(firstUserEmail, firstUserPassword);
            storeName = "theStoreName";
            storeEmail = "theStore@gmail.com";
            sp.openStore(firstUserEmail, storeName, storeEmail);
            itemName = "PC";
            itemCategory = "COMPUTERS";
            firstItemPrice = 60;
            firstItemStock = 40;
            sp.addItemToStore(firstUserEmail, itemName, storeName, firstItemStock, firstItemPrice, itemCategory);
            sp.addItemToSale(firstUserEmail, storeName, itemName, SaleType.RAFFLE.name(), "01-01-2018", "30-12-2018");
            sp.makeLogout(firstUserEmail);

            secondUserId = sp.firstConnection();
            secondUserEmail = "user2@gmail.com";
            secondUserPassword = "12345678";
            sp.makeRegister(secondUserEmail, secondUserPassword);
            //sp.makeLogin(secondUserId, secondUserEmail, secondUserPassword);
            secondStoreName = "StoreTwoName";
            secondStoreEmail = "theStore@gmail.com";
            sp.openStore(secondUserEmail, secondStoreName, secondStoreEmail);
            itemName = "PC";
            itemCategory = "COMPUTERS";
            sp.addItemToStore(secondUserEmail, itemName, secondStoreName, 50, 100, itemCategory);
            sp.makeLogout(secondUserEmail);

            for (int i=0; i<NUM_OF_EXTRA_USERS; i++) {
                usersEmails[i] = "tempUser" + i + "@gmail.com";
                sp.firstConnection();
                String password = "12345678";
                nationality = NationalityBackup.UNITED_STATES;
                sp.makeRegister(usersEmails[i], password);
                sp.makeLogout(usersEmails[i]);
            }
        }
        catch (Exception e) {
            System.err.println("Error populating DB: " + e.getMessage() + "\n");
        }
    }

    @Before
    public void setUp() throws Exception {
        firstUserId = sp.firstConnection();
        if (setUpIsDone) {
            return;
        }
        populateSystemData();
        setUpIsDone = true;
    }

    @After
    public void tearDown() throws Exception {
        if (sp.getInSystemUser(firstUserEmail) != null && sp.getInSystemUser(firstUserEmail).getType() == UserType.REGISTERED) {
            sp.clearCart(firstUserEmail);
            sp.makeLogout(firstUserEmail);
        }
    }

    private void fail() {
        assertTrue(false);
    }

    @Test
    public void firstContact_1_1() {
        String res = sp.firstConnection();
        assertNotEquals("", res);
        int i = Integer.parseInt(res);
        assertEquals((Integer.parseInt(res)+1) + "", sp.firstConnection());
    }

    @Test
    public void makeRegister_1_2_Good() {
        try {
            String email = "hello@gmail.com";
            String password = "1234ASDF";
            sp.makeRegister(email, password);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void makeRegister_1_2_BadEmail() {
        try {
            sp.makeRegister("hellogmail.com", "1234ak789");
            String email = "hellogmail.com";
            String password = "1234ASDF";
            sp.makeRegister(email, password);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void makeRegister_1_2_UsedEmail() {
        try {
            String userId = sp.firstConnection();
            sp.makeRegister(userId, "unique@gmail.com");
            sp.makeRegister("unique@gmail.com", "123qweasd");
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void makeRegister_1_2_BadPassword() {
        try {
            String email = "hello420@gmail.com";
            String password = "123ASDF";
            sp.makeRegister(email, password);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void getAllStores_1_3() {
        try {
            int originalSize = sp.getAllStores().size();
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.openStore(firstUserEmail, "brandNewStore", "email@gmail.com");
            List<Store> stores = sp.getAllStores();
            assertEquals(originalSize+1, stores.size());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getMyStores_2_2() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            List<Store> stores = sp.getMyStores(firstUserEmail);
            assertEquals(1, stores.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getStoreByName_1_3() {
        Store store = sp.getStoreByName(storeName);
        assertEquals(storeName, store.getName());
        assertEquals(storeEmail, store.getStoreEmail());
    }

    @Test
    public void getStoresItems_1_3() {
        try {
            String newItemName = "newUniqueItem";
            ArrayList<Item> items = sp.getStoresItems(storeName);
            int originalSize = items.size();
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, 80, 99, CategoryType.COMPUTERS.name());
            items = sp.getStoresItems(storeName);
            assertEquals(originalSize+1, items.size());
            assertEquals(newItemName, items.get(items.size()-1).getName());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getStoresSaleItems_1_3() {
        Set<SaleItem> saleItems = sp.getStoresSaleItems(storeName);
        assertEquals(1, saleItems.size());
        //assertEquals(itemName, saleItems.get(0).getItem().getName());
    }

    @Test
    public void getSaleItemsByItemInStore_1_3() {
        try {
            Set<SaleItem> saleItems = sp.getSaleItemsByItemInStore(itemName, storeName);
            assertEquals(1, saleItems.size());
            //assertEquals(itemName, saleItems.get(0).getItem().getName());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getAllItems_1_3() {
        List allItems = sp.getAllItems();
    }

    @Test
    public void getAllSaleItems_1_3() {
        List allSaleItems = sp.getAllSaleItems();
    }

    @Test
    public void removeSaleItemFromStore_3_1() {
        try {
            String newItemName = "love";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, 50, 100, CategoryType.CHOCOLATES.name());
            sp.addItemToSale(firstUserEmail, storeName, newItemName, SaleType.RAFFLE.name(), "01-02-2019", "01-05-2019");
            int numOfSaleItems = sp.getStoresSaleItems(storeName).size();
            sp.removeSaleItemFromStore(firstUserEmail, newItemName, storeName, SaleType.RAFFLE.name());
            int numOfSaleItemsAfterRemoval = sp.getStoresSaleItems(storeName).size();
            assertEquals(numOfSaleItems-1, numOfSaleItemsAfterRemoval);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getStoresItemsInCategory_1_3() {
       // ArrayList<SaleItem> items = sp.getStoresItemsInCategory(storeName, itemCategory);
       // assertEquals(1, items.size());
       // assertEquals(itemName, items.get(0).getItem().getName());
    }

    @Test
    public void getStoresItemsInCategory_1_3_Empty() {
        //ArrayList<SaleItem> items = sp.getStoresItemsInCategory(storeName, "fake category");
        //assertEquals(0, items.size());
    }

    @Test
    public void addToCart_1_5_Good() {
        try {
            sp.addToCart(firstUserId, storeName, itemName, 5, saleTypeNormal);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addToCart_1_5_BadStoreName() {
        String badStoreName = "ThisStoreDoesn'tExists";
        try {
            sp.addToCart(firstUserId, badStoreName, itemName, 5, saleTypeNormal);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void addToCart_1_5_BadItemName() {
        String badItemName = "MadeUpItemName";
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addToCart(firstUserEmail, storeName, badItemName, 5, saleTypeNormal);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void addToCart_1_5_BadAmount() {
        try {
            sp.addToCart(firstUserEmail, storeName, itemName, -5, saleTypeNormal);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void clearCart_1_6() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.clearCart(firstUserEmail);
            assertEquals(0, sp.getAllItemsFromCart(firstUserEmail).size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getAllItemsFromCart_1_6() {
        try {
            Set<CartItem> items = sp.getAllItemsFromCart(firstUserId);
            int size = items.size();
            sp.addToCart(firstUserId, storeName, itemName, 5, saleTypeNormal);
            items = sp.getAllItemsFromCart(firstUserId);
            assertEquals(size+1, items.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeFromCart_1_6() {
        try {
            int originalSize = sp.getAllItemsFromCart(firstUserId).size();
            sp.addToCart(firstUserId, storeName, itemName, 10, saleTypeNormal);
            sp.removeFromCart(firstUserId,itemName,storeName,saleTypeNormal);
            Set items = sp.getAllItemsFromCart(firstUserId);
            assertEquals(originalSize, items.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void changeCartItemAmount_1_6() {
        try {
            sp.addToCart(firstUserId, storeName, itemName, 10, saleTypeNormal);
            sp.changeCartItemAmount(firstUserId, itemName, storeName, 20);
            Set<CartItem> items = sp.getAllItemsFromCart(firstUserId);
            //assertEquals(20, items.get(0).getAmount());
            sp.removeFromCart(firstUserId,itemName,storeName,saleTypeNormal);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void changeCartItemPercent_1_6() {
        try {
            sp.addItemToSale(firstUserEmail,storeName,itemName,SaleType.RAFFLE.name(), "01-01-2018", "31-01-2018");
            sp.addToCart(firstUserId, storeName, itemName, 10, SaleType.RAFFLE.name());
            sp.changeCartItemPercent(firstUserId, itemName, storeName, 20);
            Set<CartItem> items = sp.getAllItemsFromCart(firstUserId);
            //assertEquals(20, items.get(0).getAmount());
            sp.removeFromCart(firstUserId, itemName, storeName, SaleType.RAFFLE.name());
            sp.removeSaleItemFromStore(firstUserEmail,itemName,storeName, SaleType.RAFFLE.name());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void checkout_1_7_NormalSaleTypeGood() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addToCart(firstUserEmail, storeName, itemName, 1, SaleType.NORMAL.name());
            sp.checkout(firstUserEmail, "00000000456010234", 20, 05, 688, "Shahar", "123456789", "Rager", NationalityBackup.ISRAEL.name());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void checkout_1_7_RaffleSaleTypeGood() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addToCart(firstUserEmail, storeName, itemName, 1, SaleType.RAFFLE.name());
            sp.checkout(firstUserEmail, "00000000456010234", 20, 05, 688, "Shahar", "123456789", "Rager", NationalityBackup.ISRAEL.name());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void checkout_1_7_BadBecauseOfPolicyMaxAmount() {
        try {
            //sp.addPurchaseRule(firstUserEmail, itemName, false, null, RuleType.MAX_AMOUNT.name(), 1, null, storeName, itemCategory);
            sp.addToCart(firstUserEmail, storeName, itemName, 2, SaleType.NORMAL.name());
            sp.checkout(firstUserEmail, "00000000456010234", 20, 05, 688, "Shahar", "123456789", "Rager", NationalityBackup.ISRAEL.name());
            fail();
        } catch (Exception ignored) {

        } finally {
            try {
                sp.removePurchaseRule(firstUserEmail, itemName, storeName, itemCategory);
            } catch (Exception ignored) {

            }
        }
    }

    @Test
    public void checkout_1_7_GoodWithPolicyMinAmount() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addPurchaseRule(firstUserEmail, itemName, false, null, RuleType.MIN_AMOUNT.name(), 2, null, storeName, itemCategory);
            sp.addToCart(firstUserEmail, storeName, itemName, 2, SaleType.NORMAL.name());
            sp.checkout(firstUserEmail, "00000000456010234", 20, 05, 688, "Shahar", "123456789", "Rager", NationalityBackup.ISRAEL.name());
        } catch (Exception e) {
            fail();
        } finally {
            try {
                sp.removePurchaseRule(firstUserEmail, itemName, storeName, itemCategory);
            } catch (Exception ignored) {

            }
        }
    }

    @Test
    public void makeLogin_2_1_Good() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void makeLogin_2_1_BadAlreadyLoggedIn() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void makeLogin_2_1_BadValidation() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, "WrongPassword");
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void openStore_2_2_Good() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.openStore(firstUserEmail, "NewStore", "NewEmail@gmail.com");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void openStore_2_2_BadStoreNameTaken() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.openStore(firstUserEmail, "UniqueNameForStore", "NewEmail@gmail.com");
            sp.openStore(firstUserEmail, "UniqueNameForStore", "NewEmail@gmail.com");
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void addItemToStore_3_1_Good() {
        try {
            String newItemName = "NewItem";
            int amount = 6;
            double price = 100;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, amount, price, itemCategory);
            Item foundItem = sp.getItemFromStore(newItemName, storeName);
            assertEquals(newItemName, foundItem.getName());
            assertEquals((int)price, (int)foundItem.getPrice());
            sp.removeItemFromStore(firstUserEmail, newItemName, storeName);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addItemToStore_3_1_BadAmount() {
        try {
            String newItemName = "NewItem";
            int amount = -1;
            double price = 100;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, amount, price, itemCategory);
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void addItemToStore_3_1_BadPrice() {
        try {
            String newItemName = "NewItem";
            int amount = 10;
            double price = -90;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, amount, price, itemCategory);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void addItemToStore_3_1_BadNoPrivileges() {
        try {
            String newItemName = "NewItem";
            int amount = 10;
            double price = 50;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, secondStoreName, amount, price, itemCategory);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void removeItemFromStore_3_1_Good() {
        try {
            String newItemName = "theBestNewItem";
            int amount = 6;
            double price = 100;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addItemToStore(firstUserEmail, newItemName, storeName, amount, price, itemCategory);
            sp.removeItemFromStore(firstUserEmail, newItemName, storeName);
            Item foundItem = sp.getItemFromStore(newItemName, storeName);
            assertEquals(null, foundItem);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void editStoreItemPrice_3_1_Good() {
        try {
            int newPrice = 20;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemPrice(firstUserEmail, itemName, storeName, newPrice);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void editStoreItemPrice_3_1_BadPrice() {
        try {
            int newPrice = -20;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemPrice(firstUserEmail, itemName, storeName, newPrice);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void editStoreItemStock_3_1_Good() {
        try {
            int newAmount = 20;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemStock(firstUserEmail, itemName, storeName, newAmount);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void editStoreItemStock_3_1_BadAmount() {
        try {
            int newAmount = -1;
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemStock(firstUserEmail, itemName, storeName, newAmount);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void editStoreItemIsOnSale_3_1_GoodTrue() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemIsOnSale(firstUserEmail, itemName, storeName, true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void editStoreItemIsOnSale_3_1_GoodFalse() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.editStoreItemIsOnSale(firstUserEmail, itemName, storeName, false);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addItemDiscount_3_2_GoodVisible() {
        try {
            String discountType = DiscountType.VISIBLE.name();
            String startDate = "01-01-2020";
            String endDate = "31-01-2020";
            String percentOff = "50";
            String extra = "";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            //sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addItemDiscount_3_2_GoodHidden() {
        try {
            String discountType = DiscountType.HIDDEN.name();
            String startDate = "01-01-2020";
            String endDate = "31-01-2020";
            String percentOff = "50";
            String extra = "167908";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            //sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addItemDiscount_3_2_BadDates() {
        try {
            String discountType = DiscountType.VISIBLE.name();
            String startDate = "31-01-2020";
            String endDate = "01-01-2020";
            String percentOff = "50";
            String extra = "";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
           // sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void addItemDiscount_3_2_BadDPercentOffMinus() {
        try {
            String discountType = DiscountType.VISIBLE.name();
            String startDate = "01-01-2020";
            String endDate = "31-01-2020";
            String percentOff = "-1";
            String extra = "";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
           // sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void addItemDiscount_3_2_BadDPercentOff101() {
        try {
            String discountType = DiscountType.VISIBLE.name();
            String startDate = "01-01-2020";
            String endDate = "31-01-2020";
            String percentOff = "101";
            String extra = "";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
          //  sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void addItemDiscount_3_2_BadHiddenEmptyCoupon() {
        try {
            String discountType = DiscountType.HIDDEN.name();
            String startDate = "01-01-2020";
            String endDate = "31-01-2020";
            String percentOff = "101";
            String extra = "";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
          //  sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void removeItemDiscount_3_2_Good() {
        try {
            String discountType = DiscountType.HIDDEN.name();
            String startDate = "01-01-2021";
            String endDate = "31-01-2021";
            String percentOff = "22";
            String extra = "111333";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
          //  sp.addItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate, percentOff);
            sp.removeItemDiscount(firstUserId, itemName, storeName, discountType, startDate, endDate);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeItemDiscount_3_2_BadNoSuchDiscount() {
        try {
            String discountType = DiscountType.VISIBLE.name();
            String startDate = "01-01-2031";
            String endDate = "31-01-2031";
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.removeItemDiscount(firstUserEmail, itemName, storeName, discountType, startDate, endDate);
            fail();
        } catch (Exception ignored) {

        }
    }


    @Test
    public void addNewStoreOwner_3_3_Good() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addNewStoreOwner(firstUserEmail, usersEmails[0], storeName);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addNewStoreOwner_3_3_BadNoPrivileges() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addNewStoreOwner(firstUserEmail, usersEmails[1], secondStoreName);
            fail();
        }
        catch (Exception ignored) {
        }
    }


    @Test
    public void addNewStoreManager_3_4_Good() {
        try {
            boolean[] privileges = {true, true, false, true, true, true, true, true, true};
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.appointManager(firstUserEmail, usersEmails[2], storeName, privileges);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addNewStoreManager_3_4_BadNotMyStore() {
        try {
            boolean[] privileges = {true, true, false, false, false, true, false, false};
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.appointManager(firstUserEmail, usersEmails[3], secondStoreName, privileges);
            fail();
        }
        catch (Exception ignored) {
        }
    }

    @Test
    public void addNewStoreManager_3_4_BadEmptyPrivileges() {
        try {
            boolean[] privileges = {};
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.appointManager(firstUserEmail, usersEmails[4], storeName, privileges);
            fail();
        }
        catch (Exception ignored) {
        }
    }

    //Version3!
    @Test
    public void closeStore_3_5_Good() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            String newStoreName = "brand new store";
            sp.openStore(firstUserEmail, newStoreName, "fakeMail@gmail.com");
            int numOfStores = sp.getMyStores(firstUserEmail).size();
            sp.closeStore(firstUserEmail, newStoreName);
            List<Store> myStores = sp.getMyStores(firstUserEmail);
            int numOfStoresAfterClose = myStores.size();
            assertEquals(false, sp.getStoreByName(newStoreName).getIsActive());
            assertEquals(numOfStores, numOfStoresAfterClose);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void getStorePurchaseHistory_3_7_Good() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.getStorePurchaseHistory(firstUserEmail, storeName);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeUserFromSystem_5_2_BadNotAdmin() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.removeUserFromSystem(firstUserEmail, secondUserEmail);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void getUserPurchaseHistory_5_4_GoodMyOwnHistory() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.getUserPurchaseHistory(firstUserEmail, firstUserEmail);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getUserPurchaseHistory_5_4_BadNotAdmin() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.getUserPurchaseHistory(firstUserEmail, secondUserEmail);
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void getCategoryDiscountsByStoreName() {
        try {
            Map categoryDiscounts = sp.getCategoryDiscountsByStoreName(storeName);
            assertEquals(0, categoryDiscounts.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addCategoryDiscountToStore() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            String startDate = "01-01-2018";
            String endDate = "01-02-2018";
            int originalSize = sp.getCategoryDiscountsByStoreName(storeName).size();
            sp.addCategoryDiscountToStore(firstUserEmail, storeName, CategoryType.CHOCOLATES.name(), DiscountType.VISIBLE.name(), startDate, endDate, "90");
            Map<CategoryType, Discounts> categoryDiscounts = sp.getCategoryDiscountsByStoreName(storeName);
            assertEquals(originalSize+1, categoryDiscounts.size());
            //assertEquals(90, (int)categoryDiscounts.get(CategoryType.CHOCOLATES).get(0).getDiscountPercent());
            sp.removeCategoryDiscountFromStore(firstUserEmail, CategoryType.CHOCOLATES.name(), storeName, DiscountType.VISIBLE.name(), startDate, endDate);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeCategoryDiscountFromStore() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            String startDate = "01-01-2018";
            String endDate = "01-02-2018";
            sp.addCategoryDiscountToStore(firstUserEmail, storeName, CategoryType.CHOCOLATES.name(), DiscountType.VISIBLE.name(), startDate, endDate, "90");
            Map<CategoryType, Discounts> categoryDiscounts = sp.getCategoryDiscountsByStoreName(storeName);
            int originalSize = categoryDiscounts.size();
            sp.removeCategoryDiscountFromStore(firstUserEmail, CategoryType.CHOCOLATES.name(), storeName, DiscountType.VISIBLE.name(), startDate, endDate);
            assertEquals(originalSize-1, categoryDiscounts.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getInSystemUser() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            User user = sp.getInSystemUser(firstUserEmail);
            assertEquals(firstUserEmail, user.getEmail());
            sp.makeLogout(firstUserEmail);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeInSystemUser() {
        try {
            String uniqueMail = "daniel@gmail.com";
            assertEquals(null, sp.getInSystemUser(uniqueMail));
            sp.makeRegister(uniqueMail, "12345678");
            assertEquals(uniqueMail, sp.getInSystemUser(uniqueMail).getEmail());
            sp.removeInSystemUser(uniqueMail);
            assertEquals(null, sp.getInSystemUser(uniqueMail));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getStoresItemsInCategory() {
        try {
            Set<SaleItem> saleItems = sp.getStoresItemsInCategory(storeName, itemCategory);
            assertEquals(1, saleItems.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getSaleItemsByItemName() {
        try {
            ArrayList<SaleItem> saleItems = sp.getSaleItemsByItemName(itemName);
            assertEquals(1, saleItems.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getItemDiscountsVisible() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            int originalNumOfDiscounts = sp.getItemDiscounts(firstUserEmail, storeEmail, itemName).size();
            sp.addItemDiscount(firstUserEmail, itemName, storeName, DiscountType.VISIBLE.name(), "01-01-2018", "01-01-2019", "50");
            Set<Discount> discounts = sp.getItemDiscounts(firstUserEmail, storeEmail, itemName);
            assertEquals(originalNumOfDiscounts+1, discounts.size());
            sp.removeItemDiscount(firstUserEmail, itemName, storeName, DiscountType.VISIBLE.name(), "01-01-2018", "01-01-2019");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getItemDiscountsHidden() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            int originalNumOfDiscounts = sp.getItemDiscounts(firstUserEmail, storeEmail, itemName).size();
            sp.addItemDiscount(firstUserEmail, itemName, storeName, DiscountType.HIDDEN.name(), "01-01-2018", "01-01-2019", "50");
            Set<Discount> discounts = sp.getItemDiscounts(firstUserEmail, storeEmail, itemName);
            assertEquals(originalNumOfDiscounts+1, discounts.size());
            sp.removeItemDiscount(firstUserEmail, itemName, storeName, DiscountType.HIDDEN.name(), "01-01-2018", "01-01-2019");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addPurchaseRule_6_Good_and_remove() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addPurchaseRule(firstUserEmail, itemName, false, null, RuleType.MAX_AMOUNT.name(), 3, null, storeName, null);
            sp.removePurchaseRule(firstUserEmail, itemName, storeName, null);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addPurchaseRule_6_BadNotOwner() {
        try {
            sp.makeLogin(secondUserId, secondUserEmail, secondUserPassword);
            sp.addPurchaseRule(secondUserEmail, itemName, false, null, RuleType.MAX_AMOUNT.name(), 3, null, storeName, null);
            fail();
        } catch (Exception ignored) {
            
        }
    }

    @Test
    public void getPurchaseRule() {
        try {
            sp.makeLogin(firstUserId, firstUserEmail, firstUserPassword);
            sp.addPurchaseRule(firstUserEmail, itemName, false, null, RuleType.MAX_AMOUNT.name(), 3, null, storeName, null);
            String literalPP = sp.getLiteralPurchaseRule(itemName, storeName, itemCategory);
            sp.removePurchaseRule(firstUserId, itemName, storeName, null);
            assertEquals("When purchasing the item PC, a customer must:\npurchase an amount of 3 or below,", literalPP);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getUserPrivilages() {
        try {
            //sp.getUserPrivilages(storeName, firstUserId);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getAppointmentsHistory() {
        try {
            Iterator<TreeNode<User>> iter = sp.getAppointmentsHistory(storeName);
            assertTrue(iter.hasNext());
            TreeNode next = iter.next();
            assertNull(next.parent);
            assertEquals(0, next.children.size());
        } catch (Exception e) {
            fail();
        }
    }
}