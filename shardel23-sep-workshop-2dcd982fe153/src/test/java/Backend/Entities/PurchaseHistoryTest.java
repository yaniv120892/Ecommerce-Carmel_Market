package Backend.Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PurchaseHistoryTest {

    PurchaseHistory purchaseHistory;
    @Before
    public void setUp() throws Exception {
        purchaseHistory = new PurchaseHistory("test", "test", "test", 10, 10, "test");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getItemName() {
        assertTrue(purchaseHistory.getItemName().equals("test"));
    }

    @Test
    public void getUserEmail() {
        assertTrue(purchaseHistory.getUserEmail().equals("test"));

    }

    @Test
    public void getStoreName() {
        assertTrue(purchaseHistory.getStoreName().equals("test"));

    }

    @Test
    public void getAmountBought() {
        assertTrue(purchaseHistory.getAmountBought() == 10);

    }

    @Test
    public void getCost() {
        assertTrue(purchaseHistory.getAmountBought() == 10);
    }

    @Test
    public void getDescription() {
        assertTrue(purchaseHistory.getDescription().equals("test"));

    }
}