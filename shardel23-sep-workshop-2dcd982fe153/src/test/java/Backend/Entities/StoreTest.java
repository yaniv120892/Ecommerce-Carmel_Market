package Backend.Entities;

import Backend.Entities.Discount.Discount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.DiscountType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class StoreTest {

    private String storeName = "roy's shop";
    private String storeEmail = "roy@gmail.com";
    private Store store = new Store(storeName,storeEmail);
    private String itemName = "roytest";

    @Test
    public void getNameAndGetEmail() {
        assertEquals(store.getName(),storeName);
        assertEquals(store.getStoreEmail(),storeEmail);
    }

    @Test
    public void getStoreSaleItems() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
            store.addItemToSale(itemName, SaleType.NORMAL.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Set<SaleItem> saleItems = store.getStoreSaleItems();
        assertEquals(saleItems.size(),1);
        for (SaleItem saleItem: saleItems) {
            assertEquals(saleItem.getItem().getName(),itemName);
        }
    }

    @Test
    public void getStoreItems() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
            store.addItemToSale(itemName, SaleType.NORMAL.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Set<SaleItem> saleItems = store.getStoreSaleItems();
        assertEquals(saleItems.size(),1);
        for (SaleItem item: saleItems) {
            assertEquals(item.getItem().getName(),itemName);
        }
    }

    @Test
    public void getStoreItem() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Item item = store.getStoreItem(itemName);
        assertNotEquals(item,null);
    }

    @Test
    public void removeItemFromStore() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Item item = store.getStoreItem(itemName);
        assertNotEquals(item,null);
        try{
            store.removeItemFromStore(itemName);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        item = store.getStoreItem(itemName);
        assertEquals(item,null);

    }

    @Test
    public void editItemPrice() {
        try {
            store.addItemToStore(itemName, 3, 10, CategoryType.CHOCOLATES.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        int newPrice = 20;
        try {
            store.editItemPrice(itemName,newPrice);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Item item = store.getStoreItem(itemName);
        assertEquals((int)item.getPrice(),newPrice);
    }

    @Test
    public void editAndGetItemStock() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        int newAmount = 10;
        try {
            store.editItemStock(itemName,newAmount);
            assertEquals( store.getItemStock(itemName),newAmount);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
    }

    @Test
    public void editItemIsOnSale() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        try {
            store.editItemIsOnSale(itemName,false);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
    }

    @Test
    public void addAndRemoveItemDiscount() {
        try {
            store.addItemToStore(itemName, 3, 10, CategoryType.CHOCOLATES.name());
            store.addItemToSale(itemName, SaleType.NORMAL.name());
            store.addItemDiscount(itemName, DiscountType.VISIBLE.name(),"01-01-2018","01-01-2019",50);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        Set<Discount> discounts = null;
        try {
            discounts = store.getItemDiscounts(itemName);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        assertEquals(discounts.size(),1);
        for (Discount discount: discounts) {
            assertEquals((int)discount.getDiscountPercent(),50);
        }
        try {
            store.removeItemDiscount(itemName,DiscountType.VISIBLE.name(),"01-01-2018","01-01-2019");
            discounts = store.getItemDiscounts(itemName);
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        assertEquals(discounts.size(),0);
    }

    @Test
    public void getStoreItemsInCategory() {
        try {
            store.addItemToStore(itemName, 3, 10.5, CategoryType.CHOCOLATES.name());
            store.addItemToStore("blondie chocolate", 4, 4.6, CategoryType.CHOCOLATES.name());
            store.addItemToStore("FML", 5, 3.2, CategoryType.BOOKS.name());
        }
        catch(Exception e){
            System.out.println("Add item to store - " + e);
            fail();
        }
        ArrayList<Item> items = store.getStoreItemsInCategory(CategoryType.CHOCOLATES.name());
        assertEquals(items.size(),2);
        for (Item item:items) {
            if(item.getName().equals(itemName) || item.getName().equals("blondie chocolate"))
                continue;
            fail();
        }
    }
}