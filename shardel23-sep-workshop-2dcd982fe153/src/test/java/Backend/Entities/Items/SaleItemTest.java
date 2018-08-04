package Backend.Entities.Items;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.SaleType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleItemTest {
    Item item;
    SaleItem saleItem;

    @Before
    public void setUp() throws Exception {
        item = new Item("test", null, 12, CategoryType.BOOKS);
        saleItem = new SaleItem(item);
    }

    @Test
    public void getItem() {
        assertTrue(item.equals(saleItem.getItem()));
    }

    @Test
    public void getType() {
        assertTrue(saleItem.getType().equals(SaleType.NORMAL));
    }
}