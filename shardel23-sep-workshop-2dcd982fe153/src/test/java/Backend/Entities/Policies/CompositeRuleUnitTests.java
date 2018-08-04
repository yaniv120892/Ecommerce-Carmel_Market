package Backend.Entities.Policies;

import Backend.Entities.Enums.LogicOperator;
import Backend.Entities.Enums.RuleType;
import Exceptions.BadAmountException;
import Exceptions.BadArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CompositeRuleUnitTests {

    private Rule maxAmountRule;
    private Rule minAmountRule;
    private Rule allowedSendingAddressRule;
    private CompositeRule maxAndMin;
    private CompositeRule maxOrAllowedSendingAddress;
    private CompositeRule notAllowedSendingAddress;
    private CompositeRule maxAndNotAllowedSendingAddress;
    private CompositeRule maxOrNotAllowedSendingAddress;

    @Before
    public void setUp() throws Exception {
        maxAmountRule = new Rule(RuleType.MAX_AMOUNT, 20, null);
        minAmountRule = new Rule(RuleType.MIN_AMOUNT, 10, null);
        allowedSendingAddressRule = new Rule(RuleType.ALLOWED_SENDING_ADDRESS, null, "ISRAEL");
        maxAndMin = new CompositeRule(minAmountRule, maxAmountRule, LogicOperator.AND);
        maxOrAllowedSendingAddress = new CompositeRule(maxAmountRule, allowedSendingAddressRule, LogicOperator.OR);
        notAllowedSendingAddress = new CompositeRule(null, allowedSendingAddressRule, LogicOperator.NOT);
        maxAndNotAllowedSendingAddress = new CompositeRule(maxAmountRule, notAllowedSendingAddress, LogicOperator.AND);
        maxOrNotAllowedSendingAddress = new CompositeRule(maxAmountRule, notAllowedSendingAddress, LogicOperator.OR);
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs1() throws Exception{
        CompositeRule rightChildNull = new CompositeRule(minAmountRule, null, LogicOperator.NOT);
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs2() throws Exception{
        CompositeRule leftChildNullNoNot = new CompositeRule(null, minAmountRule, LogicOperator.AND);
    }

    @Test(expected = BadArgumentException.class)
    public void constructorBadArgs3() throws Exception{
        CompositeRule NotOpLeftChildNotNull = new CompositeRule(maxAmountRule, minAmountRule, LogicOperator.NOT);
    }

    @Test(expected = BadAmountException.class)
    public void calculateBadAmount1() throws Exception{
        ArrayList<String> errorList = maxAndMin.calculate(0, "ISRAEL", "someItem");
    }

    @Test(expected = BadAmountException.class)
    public void calculateBadAmount2() throws Exception{
        ArrayList<String> errorList = maxAndMin.calculate(-1, "ISRAEL", "someItem");
    }

    @Test
    public void calculateGoodCase() throws Exception{
        //and operator:
        ArrayList<String> errorList1 = maxAndMin.calculate(5, "ISRAEL", "someItem");
        assertEquals(1, errorList1.size());
        ArrayList<String> errorList2 = maxAndMin.calculate(15, "ISRAEL", "someItem");
        assertEquals(0, errorList2.size());
        ArrayList<String> errorList3 = maxAndMin.calculate(25, "ISRAEL", "someItem");
        assertEquals(1, errorList3.size());


        //or operator:
        ArrayList<String> errorList4 = maxOrAllowedSendingAddress.calculate(5, "ISRAEL", "someItem");
        assertEquals(0, errorList4.size());
        ArrayList<String> errorList5 = maxOrAllowedSendingAddress.calculate(25, "ISRAEL", "someItem");
        assertEquals(0, errorList5.size());
        ArrayList<String> errorList6 = maxOrAllowedSendingAddress.calculate(5, "BLAH", "someItem");
        assertEquals(0, errorList6.size());
        ArrayList<String> errorList7 = maxOrAllowedSendingAddress.calculate(5, "USA", "someItem");
        assertEquals(0, errorList7.size());
        ArrayList<String> errorList8 = maxOrAllowedSendingAddress.calculate(25, "BLAH", "someItem");
        assertEquals(2, errorList8.size());
        ArrayList<String> errorListExtra = maxOrAllowedSendingAddress.calculate(25, "USA", "someItem");
        assertEquals(2, errorListExtra.size());

        //not operator:
        ArrayList<String> errorList9 = notAllowedSendingAddress.calculate(5, "BLAH", "someItem");
        assertEquals(0, errorList9.size());
        ArrayList<String> errorList10 = notAllowedSendingAddress.calculate(5, "UK", "someItem");
        assertEquals(0, errorList10.size());
        ArrayList<String> errorList11 = notAllowedSendingAddress.calculate(5, "ISRAEL", "someItem");
        assertEquals(1, errorList11.size());

        //and not operator:
        ArrayList<String> errorList12 = maxAndNotAllowedSendingAddress.calculate(5, "UK", "someItem");
        assertEquals(0, errorList12.size());
        ArrayList<String> errorList13 = maxAndNotAllowedSendingAddress.calculate(25, "UK", "someItem");
        assertEquals(1, errorList13.size());
        ArrayList<String> errorList14 = maxAndNotAllowedSendingAddress.calculate(5, "ISRAEL", "someItem");
        assertEquals(1, errorList14.size());
        ArrayList<String> errorList15 = maxAndNotAllowedSendingAddress.calculate(25, "ISRAEL", "someItem");
        System.out.println(errorList15.get(0));
        assertEquals(2, errorList15.size());

        //or not operator:
        ArrayList<String> errorList16 = maxOrNotAllowedSendingAddress.calculate(5, "USA", "someItem");
        assertEquals(0, errorList16.size());
        ArrayList<String> errorList17 = maxOrNotAllowedSendingAddress.calculate(25, "USA", "someItem");
        assertEquals(0, errorList17.size());
        ArrayList<String> errorList18 = maxOrNotAllowedSendingAddress.calculate(5, "ISRAEL", "someItem");
        assertEquals(0, errorList18.size());
        ArrayList<String> errorList19 = maxOrNotAllowedSendingAddress.calculate(25, "ISRAEL", "someItem");
        assertEquals(2, errorList19.size());
    }

}