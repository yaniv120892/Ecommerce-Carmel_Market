package Backend.Entities.Policies;

import Backend.Entities.Enums.LogicOperator;
import Backend.Entities.Enums.PurchasePolicyType;
import Backend.Entities.Enums.RuleType;
import Exceptions.BadArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static Backend.Entities.Enums.RuleType.MAX_AMOUNT;
import static org.junit.Assert.*;

public class PurchasePolicyUnitTests {

    private PurchasePolicy PP;

    @Before
    public void setUp() throws Exception {
        PP = new PurchasePolicy(PurchasePolicyType.STORE);
        PP.addRule("existItem", false, null, "MAX_AMOUNT", 20, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg1() throws Exception{
        PP.addRule("item1", false, "blah", "MAX_AMOUNT", 20, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg2() throws Exception{
        PP.addRule("item1", false, null, "blah", 20, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg3() throws Exception{
        PP.addRule("item1", false, null, "MAX_AMOUNT", null, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg4() throws Exception{
        PP.addRule("item1", false, null, "MIN_AMOUNT", null, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg5() throws Exception{
        PP.addRule("item1", false, null, "ALLOWED_SENDING_ADDRESS", null, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg6() throws Exception{
        PP.addRule("item1", false, null, "ALLOWED_DISCOUNT_SENDING_ADDRESS", null, null);
    }

    @Test(expected = BadArgumentException.class)
    public void addRuleBadArg7() throws Exception{  // the operator is null when the item already exist.
        PP.addRule("item1", false, null, "MAX_AMOUNT", 20, null);
        PP.addRule("item1", false, null, "MAX_AMOUNT", 30, null);
    }

    @Test
    public void addRuleGoodCase() throws Exception{
        PP.addRule("item1", false, null, "MAX_AMOUNT", 20, null);
        assertTrue(PP.getRulePerItem().containsKey("item1"));
        assertEquals(MAX_AMOUNT, ((Rule)PP.getRulePerItem().get("item1")).getRuleType());
        assertTrue(((Rule)PP.getRulePerItem().get("item1")).getNumberDetail() == 20);
        assertNull(((Rule)PP.getRulePerItem().get("item1")).getStringDetail());

        PP.addRule("item1", false, "AND", "MAX_AMOUNT", 30, null);
        assertEquals(LogicOperator.AND, ((CompositeRule)PP.getRulePerItem().get("item1")).getOperator());
        assertEquals(MAX_AMOUNT, ((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getLeftChild()).getRuleType());
        assertEquals(MAX_AMOUNT, ((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getRightChild()).getRuleType());
        assertTrue(((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getLeftChild()).getNumberDetail() == 20);
        assertTrue(((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getRightChild()).getNumberDetail() == 30);
        assertNull(((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getLeftChild()).getStringDetail());
        assertNull(((Rule)((CompositeRule)PP.getRulePerItem().get("item1")).getRightChild()).getStringDetail());

        PP.addRule("item2", true, "AND", "MAX_AMOUNT", 20, null);
        assertEquals(LogicOperator.NOT, ((CompositeRule)PP.getRulePerItem().get("item2")).getOperator());
        assertNull(((CompositeRule)PP.getRulePerItem().get("item2")).getLeftChild());
        assertEquals(MAX_AMOUNT, ((Rule)((CompositeRule)PP.getRulePerItem().get("item2")).getRightChild()).getRuleType());
        assertTrue(((Rule)((CompositeRule)PP.getRulePerItem().get("item2")).getRightChild()).getNumberDetail() == 20);
        assertNull(((Rule)((CompositeRule)PP.getRulePerItem().get("item2")).getRightChild()).getStringDetail());
    }

    @Test (expected = BadArgumentException.class)
    public void removeRuleBadArg() throws Exception{
        PP.removeRule("nonExistItem");
    }

    @Test
    public void removeRuleGoodCase() throws Exception{
        PP.removeRule("existItem");
        assertEquals(0, PP.getRulePerItem().size());
    }


    @Test (expected = BadArgumentException.class)
    public void validateItemBadAmount1() throws Exception{
        PP.validateItem("someItem", 0, "ISRAEL");
    }

    @Test (expected = BadArgumentException.class)
    public void validateItemBadAmount2() throws Exception{
        PP.validateItem("someItem", -5, "ISRAEL");
    }

    @Test
    public void validateItemGoodCase() throws Exception{
        //no rule at all:
        ArrayList<String> errorList1 = PP.validateItem("someItem", 20, "ISRAEL");
        assertEquals(0, errorList1.size());

        //only "" has a rule:
        PP.addRule("", false, null, "MAX_AMOUNT", 25, null);
        ArrayList<String> errorList2 = PP.validateItem("someItem", 20, "ISRAEL");
        assertEquals(0, errorList2.size());
        ArrayList<String> errorList3 = PP.validateItem("someItem", 30, "ISRAEL");
        assertEquals(1, errorList3.size());
        PP.removeRule("");

        //only "someItem" has a rule:
        PP.addRule("someItem", false, null, "MAX_AMOUNT", 25, null);
        ArrayList<String> errorList4 = PP.validateItem("someItem", 20, "ISRAEL");
        assertEquals(0, errorList4.size());
        ArrayList<String> errorList5 = PP.validateItem("someItem", 30, "ISRAEL");
        assertEquals(1, errorList5.size());

        //both "" and "someItem" have rules:
        PP.addRule("", false, null, "MIN_AMOUNT", 10, null);
        ArrayList<String> errorList6 = PP.validateItem("someItem", 20, "ISRAEL");
        assertEquals(0, errorList6.size());
        ArrayList<String> errorList7 = PP.validateItem("someItem", 30, "ISRAEL");
        assertEquals(1, errorList7.size());
        ArrayList<String> errorList8 = PP.validateItem("someItem", 5, "ISRAEL");
        assertEquals(1, errorList8.size());
        PP.addRule("", false, "AND", "ALLOWED_SENDING_ADDRESS", null, "ISRAEL");
        ArrayList<String> errorList9 = PP.validateItem("someItem", 30, "UK");
        assertEquals(2, errorList9.size());
    }
}