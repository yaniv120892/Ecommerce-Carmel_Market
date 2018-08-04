package Backend.Entities.Discount;

import Backend.Entities.Enums.DiscountType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public abstract class Discount {

    @Id
    @GeneratedValue
    private int discount_id;
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    protected double percentOff;
    /**
     * Including the start date
     */
    protected Date startDate;
    /**
     * Including the end date
     */
    protected Date endDate;
    protected DiscountType type;


    public Discount(String startDate, String endDate, double percentOff) throws ParseException {
        this.startDate = df.parse(startDate);
        this.endDate = df.parse(endDate);
        this.percentOff = percentOff;
    }

    protected Discount() {
    }

    public boolean isInterfering(String startDateString, String endDateString) throws ParseException {
        Date newStart = df.parse(startDateString);
        Date newEnd = df.parse(endDateString);
        return overlap(newStart, newEnd, this.startDate, this.endDate);
    }

    public boolean isSameDiscount(DiscountType discountType, String startDate, String endDate) throws ParseException {
        boolean one = isSameStartDate(startDate);
        boolean two = isSameEndDate(endDate);
        boolean three = discountType.equals(this.type);
        return one & two & three;
        //return isSameStartDate(startDate) && isSameEndDate(endDate) && discountType.equals(this.type);
    }

    public DiscountType getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isSameStartDate(String startDate) throws ParseException {
        return this.startDate.toString().equals(df.parse(startDate).toString());
        //return this.startDate.toString().equals(startDate);
    }

    public boolean isSameEndDate(String endDate) throws ParseException {
        return this.endDate.toString().equals(df.parse(endDate).toString());
        //return this.endDate.toString().equals(endDate);
    }

    /**
     * Get the discount percent. couponCode is irrelevant if the discount is visible.
     *
     * @param couponCode
     * @return the percent off.
     */
    public abstract double getDiscountPercent(String couponCode);


    public abstract double getDiscountPercent(Set<String> couponCode);

    /**
     * Checks if there is an overlap between to dates
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return true if they overlap. false otherwise
     */
    private boolean overlap(Date start1, Date end1, Date start2, Date end2) {
        return start1.getTime() <= end2.getTime() && start2.getTime() <= end1.getTime();
    }

    protected boolean isTheDiscountTimeRelevant() {
        Date now = new Date();
        return now.getTime() >= this.startDate.getTime() && now.getTime() <= this.endDate.getTime();
    }

    public double applyDiscount(double pricePerItem) {
        Date date = new Date();
        if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
            //is valid date
            //apply discount
            return pricePerItem * percentOff / 100;
        }
        return pricePerItem;
    }


    public double getDiscountPercent() {
        return percentOff;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public void setStartDate(String startDate) throws ParseException {
        this.startDate = df.parse(startDate);
    }

    public void setEndDate(String endDate) throws ParseException {
        this.endDate = df.parse(endDate);
    }

    public void setPercent(String percent) {
        this.percentOff = Double.parseDouble(percent);
    }

}
