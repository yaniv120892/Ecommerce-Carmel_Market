package Backend.Entities.Discount;

import Backend.Entities.Enums.DiscountType;

import javax.persistence.Entity;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class VisibleDiscount extends Discount {


    public VisibleDiscount(String startDate, String endDate, double percentOff) throws ParseException {
        super(startDate, endDate, percentOff);
        this.type = DiscountType.VISIBLE;
    }

    public VisibleDiscount() {
    }

    @Override
    public double getDiscountPercent(String couponCode) {
        if(isTheDiscountTimeRelevant())
            return this.percentOff;
        return 0;
    }

    @Override
    public double getDiscountPercent(Set<String> couponCodes) {
        return getDiscountPercent("");
    }

    public String getCouponsCode(){
        return "";
    }
}
