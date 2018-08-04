package Backend.Addons.PaymentService;

public class PaymentService implements PaymentServiceInterface {


    public boolean makePayment(double amount, String cardNumber){
        return true;
    }

    public boolean isAvailable() {
        return true;
    }
}
