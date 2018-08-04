package Backend.Entities.Policies;

import Backend.Entities.Enums.RuleType;
import Backend.Entities.Enums.States;
import Exceptions.BadAmountException;
import Exceptions.BadArgumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RuleUnitTests {

    private Rule maxAmountRule;
    private Rule minAmountRule;
    private Rule allowedSendingAddressRule;
    private Rule allowedDiscountSendingAddressRule;

    @Before
    public void setUp() throws Exception {
        maxAmountRule = new Rule(RuleType.MAX_AMOUNT, 10, null);
        minAmountRule = new Rule(RuleType.MIN_AMOUNT, 10, null);
        allowedSendingAddressRule = new Rule(RuleType.ALLOWED_SENDING_ADDRESS, null, "ISRAEL");
        allowedDiscountSendingAddressRule = new Rule(RuleType.ALLOWED_DISCOUNT_SENDING_ADDRESS, null, "ISRAEL");
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs1() throws Exception{
        Rule badSendingAddress = new Rule(RuleType.ALLOWED_SENDING_ADDRESS, null, "BLAH");
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs2() throws Exception{
        Rule badAmount1 = new Rule(RuleType.MAX_AMOUNT, 0, null);
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs3() throws Exception{
        Rule badAmount2 = new Rule(RuleType.MAX_AMOUNT, -5, null);
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs4() throws Exception{
        Rule badAmount3 = new Rule(RuleType.MIN_AMOUNT, -2, null);

    }

    @Test(expected = BadAmountException.class)
    public void calculateBadAmount1() throws Exception{
        ArrayList<String> errorList = maxAmountRule.calculate(0, "ISRAEL", "someItem");
    }

    @Test(expected = BadAmountException.class)
    public void calculateBadAmount2() throws Exception{
        ArrayList<String> errorList = maxAmountRule.calculate(-1, "ISRAEL", "someItem");
    }

    @Test
    public void calculateGoodCases() throws Exception{
        //maxAmount:
        ArrayList<String> errorList1 = maxAmountRule.calculate(9, "ISRAEL", "someItem");
        assertEquals(0, errorList1.size());
        ArrayList<String> errorList2 = maxAmountRule.calculate(11, "ISRAEL", "someItem");
        assertEquals(1, errorList2.size());

        //minAmount:
        ArrayList<String> errorList3 = minAmountRule.calculate(9, "ISRAEL", "someItem");
        assertEquals(1, errorList3.size());
        ArrayList<String> errorList4 = minAmountRule.calculate(11, "ISRAEL", "someItem");
        assertEquals(0, errorList4.size());

        //allowedSendingAddress:
        ArrayList<String> errorList5 = allowedSendingAddressRule.calculate(5, "ISRAEL", "someItem");
        assertEquals(0, errorList5.size());
        ArrayList<String> errorList6 = allowedSendingAddressRule.calculate(5, "BLAH", "someItem");
        assertEquals(1, errorList6.size());
        ArrayList<String> errorList7 = allowedSendingAddressRule.calculate(5, "USA", "someItem");
        assertEquals(1, errorList7.size());

        //allowedDiscountSendingAddress:
        ArrayList<String> errorList8 = allowedDiscountSendingAddressRule.calculate(5, "ISRAEL", "someItem");
        assertEquals(0, errorList8.size());
        ArrayList<String> errorList9 = allowedDiscountSendingAddressRule.calculate(5, "BLAH", "someItem");
        assertEquals(1, errorList9.size());
        ArrayList<String> errorList10 = allowedDiscountSendingAddressRule.calculate(5, "USA", "someItem");
        assertEquals(1, errorList10.size());

    }
}