package Backend.Entities.Items;

import Backend.Entities.Enums.CategoryType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {


    Item item;

    @Before
    public void setUp() throws Exception {
        item = new Item("test", null, 12, CategoryType.BOOKS);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertTrue(item.getName().equals("test"));
    }

    @Test
    public void getStore() {
        assertNull(item.getStore());
    }

    @Test
    public void getOriginalPrice() {
        assertTrue(item.getOriginalPrice() == 12);
    }

    @Test
    public void changePrice() {
        item.changePrice(13);
        assertTrue(item.getOriginalPrice() == 13);
        item.changePrice(12);
    }

    @Test
    public void getCategory() {
        assertTrue(item.getCategory().equals(CategoryType.BOOKS));
    }
}