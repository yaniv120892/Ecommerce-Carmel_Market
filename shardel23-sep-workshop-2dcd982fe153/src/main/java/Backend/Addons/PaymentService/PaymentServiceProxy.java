package Backend.Addons.PaymentService;

import Backend.Addons.EventLogger;

public class PaymentServiceProxy implements PaymentServiceInterface{

    PaymentService paymentService;

    public PaymentServiceProxy() {
        paymentService = new PaymentService();
    }

    @Override
    public boolean makePayment(double amount, String cardNumber) {
        EventLogger.eventLogger.info("A payment for "+amount+" with card "+cardNumber + " has been requested");
        if(paymentService.isAvailable()){
            return paymentService.makePayment(amount, cardNumber);
        }
        return false;
    }
}
