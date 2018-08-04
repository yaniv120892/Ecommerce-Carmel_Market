package Backend.Addons;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Items.Item;
import Exceptions.BadPasswordException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void validateEmailAddress() throws Exception {
        assertTrue(Util.validateEmailAddress("abc@gmail.com"));
        assertTrue(Util.validateEmailAddress("abc@gmail.co.il"));
        assertTrue(Util.validateEmailAddress("abc@gmail.ru"));
        assertFalse(Util.validateEmailAddress("b.c.g.rr"));
        assertFalse(Util.validateEmailAddress("123456789"));
    }

    @Test
    public void validatePassword() {
        try {
            assertTrue(Util.validatePassword("12345678"));
        } catch (BadPasswordException e) {
            assertFalse(true);
        }
        try {
            assertTrue(Util.validatePassword("123456789"));
        } catch (BadPasswordException e) {
            assertFalse(true);
        }
        try {
            assertTrue(Util.validatePassword("fjhkdshfjksdhfjksd"));
        } catch (BadPasswordException e) {
            assertFalse(true);
        }
        try {
            assertTrue(Util.validatePassword("ccccccccccccc"));
        } catch (BadPasswordException e) {
            assertFalse(true);
        }
        try {
            assertTrue(Util.validatePassword("GHJFYT&*FHJF*(%&^FGK"));
        } catch (BadPasswordException e) {
            assertFalse(false);
        }
        try {
            assertFalse(Util.validatePassword("1234567"));
        } catch (BadPasswordException e) {
            assertFalse(false);
        }
        try {
            assertFalse(Util.validatePassword("abcdef"));
        } catch (BadPasswordException e) {
            assertFalse(false);
        }
        try {
            assertFalse(Util.validatePassword(""));
        } catch (BadPasswordException e) {
            assertFalse(false);
        }
        try {
            assertFalse(Util.validatePassword("444"));
        } catch (BadPasswordException e) {
            assertFalse(false);
        }
    }

    @Test
    public void findClosestItemMatchGoodResult() throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("playstation4", null, 0, CategoryType.BOOKS));
        items.add(new Item("stations", null, 0, CategoryType.BOOKS));
        items.add(new Item("playground", null, 0, CategoryType.BOOKS));
        items.add(new Item("popcorn", null, 0, CategoryType.BOOKS));
        items.add(new Item("playboys", null, 0, CategoryType.BOOKS));
        String targetName = "olaystation";
        Item result = Util.findClosestItemMatch(targetName, items);
        assertEquals("playstation4", result.getName());
    }

    @Test
    public void findClosestItemMatchEmptyResult() throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("playstation4", null, 0, CategoryType.BOOKS));
        items.add(new Item("stations", null, 0, CategoryType.BOOKS));
        items.add(new Item("playground", null, 0, CategoryType.BOOKS));
        items.add(new Item("popcorn", null, 0, CategoryType.BOOKS));
        items.add(new Item("playboys", null, 0, CategoryType.BOOKS));
        String targetName = "play";
        Item result = Util.findClosestItemMatch(targetName, items);
        assertEquals("playstation4", result.getName());
    }

}