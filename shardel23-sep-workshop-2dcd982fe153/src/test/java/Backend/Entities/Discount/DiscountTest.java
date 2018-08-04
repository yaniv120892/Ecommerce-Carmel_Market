package Backend.Entities.Discount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiscountTest {

    private VisibleDiscount visibleDiscount;

    @Before
    public void setUp() {
        try{
            visibleDiscount = new VisibleDiscount("10-10-2010","10-11-2010",10);
        }
        catch(Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testOverlap() {
        try {
            Assert.assertTrue(visibleDiscount.isInterfering("9-10-2010","9-10-2011"));
            Assert.assertTrue(visibleDiscount.isInterfering("9-10-2010","25-10-2010"));
            Assert.assertTrue(visibleDiscount.isInterfering("9-10-2010","1-11-2010"));
            Assert.assertTrue(visibleDiscount.isInterfering("9-10-2010","1-11-2011"));
            Assert.assertFalse(visibleDiscount.isInterfering("9-10-2010","9-10-2010"));
        }
        catch (Exception e){
            Assert.fail();
        }


    }
}