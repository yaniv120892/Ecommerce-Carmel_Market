package Backend.Entities.Users;

import Backend.Data.DBInterface;
import Backend.Data.ListData;
import Backend.Entities.Enums.UserType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdminUserTest {

    DBInterface data;
    User user;

    @Before
    public void setUp() throws Exception {
        data = new ListData();
        user = new AdminUser(data);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getType() {
        assertTrue(user.getType().equals(UserType.ADMIN));
    }

    @Test(expected = Exception.class)
    public void addToCart() throws Exception {
        user.addToCart(null, 10, null);
    }

    @Test(expected = Exception.class)
    public void getMyCartItems() throws Exception {
        user.getMyCartItems();
    }

    @Test(expected = Exception.class)
    public void removeFromCart() throws Exception {
        user.removeFromCart(null, null, null);
    }

    @Test(expected = Exception.class)
    public void changeCartItemAmount() throws Exception {
        user.changeCartItemAmount(null, null, 10,null);
    }

    @Test(expected = Exception.class)
    public void addStoreAsOwner() throws Exception {
        user.addStoreAsOwner(null);
    }

    @Test(expected = Exception.class)
    public void addItemToStore() throws Exception {
        user.addItemToStore(null,null, 10, 12, null);
    }

    @Test(expected = Exception.class)
    public void removeItemFromStore() throws Exception {
        user.removeItemFromStore(null, null);
    }

    @Test(expected = Exception.class)
    public void editStoreItemPrice() throws Exception {
        user.editStoreItemPrice(null, null ,12);
    }

    @Test(expected = Exception.class)
    public void editStoreItemStock() throws Exception {
        user.editStoreItemStock(null, null , 12);
    }

    @Test(expected = Exception.class)
    public void editStoreItemIsOnSale() throws Exception {
        user.editStoreItemIsOnSale(null, null, false);
    }

    @Test(expected = Exception.class)
    public void addItemDiscount() throws Exception {
        user.addItemDiscount(null, null, null,null,null,null);
    }

    @Test(expected = Exception.class)
    public void removeItemDiscount() throws Exception {
        user.removeItemDiscount(null,null,null,null,null);
    }

    @Test(expected = Exception.class)
    public void appointNewOwner() throws Exception {
        user.appointNewOwner(null,null);
    }

    @Test(expected = Exception.class)
    public void appointManager() throws Exception {
        user.appointManager(null,null,null);
    }

    @Test(expected = Exception.class)
    public void checkout() throws Exception {
        user.checkout(null,null,null, "");
    }

    @Test(expected = Exception.class)
    public void clearCart() throws Exception {
        user.clearCart();
    }
}