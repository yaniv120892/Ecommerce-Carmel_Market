package Backend.Entities;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.CartItem;
import Backend.Entities.Items.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CartTest {

    private Cart cart;

    public CartTest() {
        cart = new Cart();
    }

    @Before
    public void setUp() throws Exception {
        cart = new Cart();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addItem1() {
        String itemName = "name";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int price = 10;
        CategoryType categoryType = CategoryType.BOOKS;
        SaleType saleType = SaleType.NORMAL;
        Item item = new Item(itemName, store, price, categoryType);
        cart.addItem(new CartItem(item, 5, saleType));
        Set<CartItem> items = cart.getItems();
        assertTrue(((CartItem)items.toArray()[items.size()-1]).getItem().equals(item));
    }

    @Test
    public void addItem2() {
        String itemName = "name";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int price = 10;
        CategoryType categoryType = CategoryType.BOOKS;
        SaleType saleType = SaleType.NORMAL;
        Item item = new Item(itemName, store, price, categoryType);
        cart.addItem(item, 5, saleType.name());
        Set<CartItem> items = cart.getItems();
        assertTrue(((CartItem)items.toArray()[items.size()-1]).getItem().equals(item));
    }

    @Test
    public void getItems() {
        int originalSize = cart.getItems().size();
        String itemName = "name";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int price = 10;
        CategoryType categoryType = CategoryType.BOOKS;
        SaleType saleType = SaleType.NORMAL;
        Item item = new Item(itemName, store, price, categoryType);
        cart.addItem(item, 5, saleType.name());
        Set<CartItem> items = cart.getItems();
        assertEquals(originalSize+1, items.size());
    }

    @Test
    public void removeItem() throws Exception {
        String itemName = "name";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int price = 10;
        CategoryType categoryType = CategoryType.BOOKS;
        SaleType saleType = SaleType.NORMAL;
        Item item = new Item(itemName, store, price, categoryType);
        cart.addItem(item, 5, saleType.name());
        int originalSize = cart.getItems().size();
        cart.removeItem(itemName,storeName,saleType.name());
        Set<CartItem> items = cart.getItems();
        assertEquals(originalSize-1, items.size());
    }

    @Test
    public void changeCartItemAmount() throws Exception {
        int newAmount = 100;
        String itemName = "name";
        String storeName = "store";
        Store store = new Store(storeName, "email@gmail.com");
        int price = 10;
        CategoryType categoryType = CategoryType.BOOKS;
        SaleType saleType = SaleType.NORMAL;
        Item item = new Item(itemName, store, price, categoryType);
        cart.addItem(item, 5, saleType.name());
        cart.changeCartItemAmount(itemName, storeName, newAmount, saleType);
        Set<CartItem> items = cart.getItems();
        assertEquals(newAmount, ((CartItem)items.toArray()[items.size()-1]).getAmount());
    }

}