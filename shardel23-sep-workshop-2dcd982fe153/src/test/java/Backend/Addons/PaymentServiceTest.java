package Backend.Addons;

import Backend.Addons.PaymentService.PaymentService;
import Backend.Addons.PaymentService.PaymentServiceInterface;
import Backend.Addons.PaymentService.PaymentServiceProxy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentServiceTest {

    PaymentServiceInterface paymentService;
    @Before
    public void setUp() throws Exception {
            paymentService = new PaymentServiceProxy();
    }

    @Test
    public void makePayment() {
    assertTrue(paymentService.makePayment(50, "543534"));
    }
}