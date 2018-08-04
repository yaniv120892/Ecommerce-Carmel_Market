package Backend.Addons;

import Backend.Addons.SupplyService.SupplyService;
import Backend.Addons.SupplyService.SupplyServiceInterface;
import Backend.Addons.SupplyService.SupplyServiceProxy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SupplyServiceTest {

    SupplyServiceInterface supplyService;
    @Before
    public void setUp() throws Exception {
        supplyService = new SupplyServiceProxy();
    }

    @Test
    public void makeOrder() {
        assertTrue(supplyService.makeOrder());
    }
}