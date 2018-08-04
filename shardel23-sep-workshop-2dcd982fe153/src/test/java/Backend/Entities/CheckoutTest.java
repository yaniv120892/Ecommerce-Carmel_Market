package Backend.Entities;

import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Users.RegUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CheckoutTest {
    private String storeName = "roy's shop";
    private String storeEmail = "roy@gmail.com";
    private Store store = new Store(storeName,storeEmail);
    private String itemName = "roytest";
    private RegUser user = new RegUser();
    private Item item;
    private SaleItem saleItem = new SaleItem();

    public CheckoutTest(){
        try {
            store.addItemToStore(itemName, 10, 10, "BOOKS");
            item = store.getStoreItem(itemName);
            saleItem = new SaleItem(item);
            store.addItemToSale(itemName, SaleType.NORMAL.name());
        }
        catch (Exception e){
            fail(e.toString());
        }
    }

    @Test
    public void SaleItemCheckout() {
        saleItem = new SaleItem(item);
        try {
            saleItem.checkout(user, 5, null);
            assertEquals(5,store.getItemStock(itemName));
            store.editItemStock(itemName,10);
            assertEquals(10,store.getItemStock(itemName));

        }
        catch(Exception e){
            fail(e.toString());
        }
    }

    @Test
    public void SaleItemCheckoutFail() {
        saleItem = new SaleItem(item);
        try {
            saleItem.checkout(user, 11, null);
            fail("out of stock yet checkout worked!");

        }
        catch(Exception e){

        }
    }

    @Test
    public void StoreCheckout() {
        int amountToBuy = 1;
        try {
            int price = (int)store.checkout(user,item,amountToBuy,SaleType.NORMAL,item.getPrice(),null);
            assertEquals(10*amountToBuy,price);
            store.editItemStock(itemName,10);
            assertEquals(10,store.getItemStock(itemName));
        }
        catch(Exception e){
            fail(e.toString());
        }
    }

    @Test
    public void ItemCheckout() {
        int amountToBuy = 5;
        try {
            int price = (int)item.checkout(user,amountToBuy,SaleType.NORMAL,null);
            assertEquals(10*amountToBuy,price);
            store.editItemStock(itemName,10);
            assertEquals(10,store.getItemStock(itemName));
        }
        catch(Exception e){
            fail(e.toString());
        }
    }
}
