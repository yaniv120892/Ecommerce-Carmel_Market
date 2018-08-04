package Backend.Entities.Discount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HiddenDiscountTest {

    private HiddenDiscount hiddenDiscount;

    @Before
    public void setUp() {
        try{
            hiddenDiscount = new HiddenDiscount("29-04-2018","10-05-2018",10);
        }
        catch(Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testCouponGen() {
        String frstGen = hiddenDiscount.generateCouponCode();
        Assert.assertNotEquals(frstGen,hiddenDiscount.generateCouponCode());
    }

    @Test
    public void testCoupon() {
        try{
            hiddenDiscount = new HiddenDiscount("29-04-2018","10-05-2018",10);
        }
        catch(Exception e){
            Assert.fail();
        }
        String couponCode = hiddenDiscount.generateCouponCode();
        Assert.assertEquals(10,hiddenDiscount.getDiscountPercent(couponCode),0);
    }
}