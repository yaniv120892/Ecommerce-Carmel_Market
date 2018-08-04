package Backend.Entities.Users;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Enums.UserType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import Backend.Entities.Privileges;
import Backend.Entities.Store;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.Assert.*;

public class RegUserTest {

    private RegUser user;

    public RegUserTest() {
        user = new RegUser();
    }

    @Before
    public void setUp() throws Exception {
        user = new RegUser();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkout() {
    }

    @Test
    public void clearCartOK() {
        user.addToCart(new Item("item1", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item2", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item3", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.clearCart();
        assertEquals(0, user.getCart().getItems().size());
    }

    @Test
    public void addToCart() {
        int originalCartSize = user.getCart().getItems().size();
        user.addToCart(new Item("item1", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item2", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item3", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        assertEquals(originalCartSize + 3, user.getCart().getItems().size());
    }

    @Test
    public void getMyCartItems() {
        int originalCartSize = user.getCart().getItems().size();
        user.addToCart(new Item("item1", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item2", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item3", null, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        Set<CartItem> myCartItems = user.getMyCartItems();
        assertEquals(originalCartSize + 3, myCartItems.size());
        assertEquals("item1", ((CartItem)myCartItems.toArray()[0]).getItem().getName());
        assertEquals("item2", ((CartItem)myCartItems.toArray()[1]).getItem().getName());
        assertEquals("item3", ((CartItem)myCartItems.toArray()[2]).getItem().getName());
    }

    @Test
    public void removeFromCart() throws Exception {
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addToCart(new Item(itemName, store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item2", store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item3", store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        int originalCartSize = user.getCart().getItems().size();
        user.removeFromCart(itemName, storeName, SaleType.NORMAL.name());
        assertEquals(originalCartSize - 1, user.getCart().getItems().size());
    }

    @Test
    public void changeCartItemAmount() throws Exception {
        String itemName = "item1";
        String storeName = "store";
        int newAmount = 5;
        Store store = new Store(storeName, "email@gmail.com");
        user.addToCart(new Item(itemName, store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item2", store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.addToCart(new Item("item3", store, 0, CategoryType.BOOKS), 1, SaleType.NORMAL.name());
        user.changeCartItemAmount(itemName, storeName, newAmount, SaleType.NORMAL);
        assertEquals(newAmount, user.getCartItem(itemName, storeName).getAmount());
    }

    @Test
    public void addStoreAsOwner() {
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int originalSize = user.getStores().size();
        user.addStoreAsOwner(store);
        assertEquals(originalSize + 1, user.getStores().size());
    }

    @Test
    public void addItemToStore() throws Exception {
        int itemsStock = 50;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, 30, CategoryType.TECHNOLEGY.name());
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(itemsStock, entry.getKey().getItemStock(itemName));
                return;
            }
        }
        fail();
    }

    @Test
    public void removeItemFromStore() throws Exception {
        int itemsStock = 50;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, 30, CategoryType.TECHNOLEGY.name());
        user.removeItemFromStore(itemName, storeName);
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(0, entry.getKey().getStoreItems().size());
                return;
            }
        }
        fail();
    }

    @Test
    public void editStoreItemPrice() throws Exception {
        int itemsStock = 50;
        int itemsPrice = 30;
        int newPrice = 10;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, itemsPrice, CategoryType.TECHNOLEGY.name());
        user.editStoreItemPrice(itemName, storeName, 10);
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(newPrice, (int) entry.getKey().getStoreItem(itemName).getPrice());
                return;
            }
        }
        fail();
    }

    @Test
    public void editStoreItemStock() throws Exception {
        int itemsStock = 50;
        int itemsPrice = 30;
        int newStock = 10;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, itemsPrice, CategoryType.TECHNOLEGY.name());
        user.editStoreItemStock(itemName, storeName, newStock);
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(newStock, entry.getKey().getItemStock(itemName));
                return;
            }
        }
        fail();
    }

    @Test
    public void editStoreItemIsOnSaleFalse() throws Exception {
        int itemsStock = 50;
        int itemsPrice = 30;
        boolean newActiveValue = false;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, itemsPrice, CategoryType.TECHNOLEGY.name());
        user.editStoreItemIsOnSale(itemName, storeName, newActiveValue);
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(newActiveValue, entry.getKey().getIsActive());
                return;
            }
        }
        fail();
    }

    @Test
    public void editStoreItemIsOnSaleTrue() throws Exception {
        int itemsStock = 50;
        int itemsPrice = 30;
        boolean newActiveValue = true;
        String itemName = "item1";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        user.addStoreAsOwner(store);
        user.addItemToStore(itemName, storeName, itemsStock, itemsPrice, CategoryType.TECHNOLEGY.name());
        user.editStoreItemIsOnSale(itemName, storeName, newActiveValue);
        Map<Store, Privileges> stores = user.getStores();
        for (Entry<Store, Privileges> entry : stores.entrySet()) {
            if (entry.getKey().getName().equals(storeName)) {
                assertEquals(newActiveValue, entry.getKey().getIsActive());

                return;
            }
        }
        fail();
    }

    @Test
    public void addItemDiscount() {
    }

    /**
     * Test that a store owner appoint a new manager and check if the new manager is now a manager of the store
     * with all privileges for manager on
     * @throws Exception
     */
    @Test
    public void appointNewManagerSuccessAllPrivilegesOn() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        user.addStoreAsOwner(store);
        store.addOwner(null,user);
        User newManager = new RegUser();
        newManager.setEmail("newUser");
        newManager.setType(UserType.REGISTERED);
        boolean[] privileges = new boolean[9];
        for (int i = 0; i < privileges.length; i++) {
            privileges[i] = true;
        }
        user.appointManager(newManager, storeName, privileges);
        assertTrue(newManager.isOwnerOrManager(storeName));
        Privileges newManagerPrivileges = newManager.getStorePrivileges(storeName);
        boolean[] privilegesArr = newManagerPrivileges.getPrivilegesAsBooleanArrForManager();
        for (int i = 0; i < privilegesArr.length; i++) {
            assertEquals(privilegesArr[i], privileges[i]);
        }
        assertFalse(newManagerPrivileges.canRemoveSaleItemFromStore());
        assertFalse(newManagerPrivileges.canRemoveCategoryDiscount());
        assertFalse(newManagerPrivileges.canEditDiscountForItem());
        assertFalse(newManagerPrivileges.canCloseStore());
        assertFalse(newManagerPrivileges.canAddOwner());
        assertFalse(newManagerPrivileges.canEditCategoryDiscountForItem());
    }

    /**
     * Test that a store owner appoint a new manager and check if the new manager is now a manager of the store
     * with all privileges for manager on
     * @throws Exception
     */
    @Test
    public void appointNewManagerSuccessAllPrivilegesOff() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        user.addStoreAsOwner(store);
        store.addOwner(null,user);
        User newManager = new RegUser();
        newManager.setEmail("newUser");
        newManager.setType(UserType.REGISTERED);
        boolean[] privileges = new boolean[9];
        for (int i = 0; i < privileges.length; i++) {
            privileges[i] = false;
        }
        user.appointManager(newManager, storeName, privileges);
        assertTrue(newManager.isOwnerOrManager(storeName));
        Privileges newManagerPrivileges = newManager.getStorePrivileges(storeName);
        boolean[] privilegesArr = newManagerPrivileges.getPrivilegesAsBooleanArrForManager();
        for (int i = 0; i < privilegesArr.length; i++) {
            assertEquals(privilegesArr[i], privileges[i]);
        }
        assertFalse(newManagerPrivileges.canRemoveSaleItemFromStore());
        assertFalse(newManagerPrivileges.canRemoveCategoryDiscount());
        assertFalse(newManagerPrivileges.canEditDiscountForItem());
        assertFalse(newManagerPrivileges.canCloseStore());
        assertFalse(newManagerPrivileges.canAddOwner());
        assertFalse(newManagerPrivileges.canEditCategoryDiscountForItem());
    }




    /**
     * Test that a store owner appoint a new owner and check if the new owner is now a owner of the store
     * with all privileges on
     * @throws Exception
     */
    @Test
    public void appointNewOwnerSuccess() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        user.addStoreAsOwner(store);
        store.addOwner(null,user);
        User newOwner = new RegUser();
        newOwner.setEmail("newUser");
        newOwner.setType(UserType.REGISTERED);
        user.appointNewOwner(storeName,newOwner);
        assertTrue(newOwner.isOwnerOrManager(storeName));
        Privileges newManagerPrivileges = newOwner.getStorePrivileges(storeName);
        boolean[] privilegesArr = newManagerPrivileges.getPrivilegesAsBooleanArrForOwner();
        for (int i = 0; i < privilegesArr.length; i++) {
            assertTrue(privilegesArr[i]);
        }
    }

    /**
     * Test to check that a user that is not owner try to appoint a new owner and get exception
     * @throws Exception
     */
    @Test
    public void appointNewOwnerFail() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        User newManager = new RegUser();
        newManager.setEmail("newUser");
        newManager.setType(UserType.REGISTERED);
        try {
            user.appointNewOwner(storeName, newManager);
            assertTrue(false);
        }
        catch (Exception e) {
        }
        assertFalse(newManager.isOwnerOrManager(storeName));
    }

    /**
     * Test to check that a user that is not owner try to appoint a new manager and get exception
     * @throws Exception
     */
    @Test
    public void appointNewManagerFail() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        User newManager = new RegUser();
        newManager.setEmail("newUser");
        newManager.setType(UserType.REGISTERED);
        try {
            boolean[] privileges = new boolean[9];
            for (int i = 0; i < privileges.length; i++) {
                privileges[i] = false;
            }
            user.appointManager(newManager, storeName, privileges);
            assertTrue(false);
        }
        catch (Exception e) {
        }
        assertFalse(newManager.isOwnerOrManager(storeName));
    }


    /**
     * Test to check that a store owner tries to appoint an unknown user as owner and get exception
     * @throws Exception
     */
    @Test
    public void appointNewOwnerFail2() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        user.addStoreAsOwner(store);
        store.addOwner(null,user);
        User newOwner = new RegUser();
        newOwner.setEmail("newUser");
        user.addStoreAsOwner(store);
        try {
            user.appointNewOwner(storeName, newOwner);
            assertTrue(false);
        }
        catch (Exception e) {
        }
        assertFalse(newOwner.isOwnerOrManager(storeName));
    }

    /**
     * Test to check that a store owner tries to appoint an unknown user as manager and get exception
     * @throws Exception
     */
    @Test
    public void appointNewManagerFail2() throws Exception {
        String storeName = "storeForTest";
        user.setEmail("user1");
        Store store = new Store(storeName, "storeForTest@gmail.com");
        user.addStoreAsOwner(store);
        store.addOwner(null,user);
        User newManager = new RegUser();
        newManager.setEmail("newUser");
        user.addStoreAsOwner(store);
        try {
            boolean[] privileges = new boolean[9];
            for (int i = 0; i < privileges.length; i++) {
                privileges[i] = false;
            }
            user.appointManager(newManager, storeName, privileges);
            assertTrue(false);
        }
        catch (Exception e) {
        }
        assertFalse(newManager.isOwnerOrManager(storeName));
    }
    @Test
    public void removeItemDiscount() {
    }
    @Test
    public void getPurchaseHistoryOfStore() {

    }

    @Test
    public void removeUserFromSystem() {
        try {
            user.removeUserFromSystem("toRemove");
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void getStores() {
        boolean found = false;
        Store newStore = new Store("coolShop", "cool@gmail.com");
        user.addStoreAsOwner(newStore);
       Map<Store, Privileges> pairs = user.getStores();
        for (Entry<Store, Privileges> pair : pairs.entrySet()) {
            if (pair.getKey() == newStore) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void addCoupon() {
        try {
            String couponCode = "123456789";
            user.addCoupon(couponCode);
            Set<String> coupons = user.getUserCoupons();
            for (String coupon : coupons) {
                assertEquals(couponCode, coupon);
            }
        } catch (Exception e) {
            fail();
        }
    }
}