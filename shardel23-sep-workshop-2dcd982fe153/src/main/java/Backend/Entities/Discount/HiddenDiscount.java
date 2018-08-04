package Backend.Entities.Discount;


import Backend.Entities.Enums.DiscountType;

import javax.persistence.Entity;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class HiddenDiscount extends Discount {

    String couponsCode;
    boolean getCouponCode;

    public HiddenDiscount(String startDate, String endDate, double percentOff) throws ParseException {
        super(startDate, endDate, percentOff);
        this.couponsCode = generateCouponCode();
        this.type = DiscountType.HIDDEN;
        this.getCouponCode = true;
    }

    public HiddenDiscount() {
    }

    public String generateCouponCode(){
        if(!this.getCouponCode)
            return "";
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(9);
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        this.getCouponCode = false;
        this.couponsCode = sb.toString();
        return sb.toString();
    }

    public String getCouponsCode(){
        return this.couponsCode;
    }

    public double getDiscountPercent(String couponCode){
        if(isTheDiscountTimeRelevant() && this.couponsCode.equals(couponCode))
            return this.percentOff;
        return 0;
    }

    public double getDiscountPercent(Set<String> couponCodes){
        for (String couponCode:couponCodes) {
            if(this.couponsCode.equals(couponCode) && isTheDiscountTimeRelevant())
                return this.percentOff;
        }
        return 0;
    }
}
