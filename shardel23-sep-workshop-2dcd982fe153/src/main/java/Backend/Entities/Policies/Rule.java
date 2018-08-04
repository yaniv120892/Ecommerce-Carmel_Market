package Backend.Entities.Policies;

import Backend.Entities.Enums.RuleType;
import Backend.Entities.Enums.States;
import Exceptions.BadAmountException;
import Exceptions.BadArgumentException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Rule implements RuleInterface{

    private int ruleId;
    @Id
    @GeneratedValue
    private int norm_id;
    private RuleType ruleType;
    private Integer numberDetail;
    private String stringDetail;

    public Rule() {
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public Integer getNumberDetail() {
        return numberDetail;
    }

    public String getStringDetail() {
        return stringDetail;
    }

    public Rule(RuleType ruleType, Integer numberDetail, String stringDetail) throws Exception{
        if((ruleType == RuleType.MAX_AMOUNT && ((numberDetail <= 0) || (numberDetail == null))) || ((ruleType == RuleType.MIN_AMOUNT && ((numberDetail < 0) || (numberDetail == null)))))
            throw new BadArgumentException("numberDetail");
        if((ruleType == RuleType.ALLOWED_SENDING_ADDRESS || ruleType == RuleType.ALLOWED_DISCOUNT_SENDING_ADDRESS) && (stringDetail == null || !States.contains(stringDetail)))
            throw new BadArgumentException("Sending Address");
        this.ruleType = ruleType;
        this.numberDetail = numberDetail;
        this.stringDetail = stringDetail;
    }

    @Override
    public int getRuleId() {
        return ruleId;
    }

    @Override
    public void setRuleId(int id) {
        ruleId = id;
    }

    public ArrayList<String> calculate(int quantity, String sendingAddress, String itemName) throws Exception{
        if(quantity <= 0) throw new BadAmountException();
        ArrayList<String> errorsList = new ArrayList<>();
        switch (ruleType){
            case MAX_AMOUNT:
                if(quantity > numberDetail)
                    errorsList.add("You are trying to purchase more than the maximum amount allowed of the item " + itemName + ".");
                break;
            case MIN_AMOUNT:
                if(quantity < numberDetail)
                    errorsList.add("You are trying to purchase less than the minimal amount allowed of the item " + itemName + ".");
                break;
            case ALLOWED_SENDING_ADDRESS:
                if(!States.contains(sendingAddress) || stringDetail != sendingAddress)
                    errorsList.add("The item " + itemName + " cannot be shipped to the specified sending address.");
                break;
            case ALLOWED_DISCOUNT_SENDING_ADDRESS:
                if(!States.contains(sendingAddress) || stringDetail != sendingAddress)
                    errorsList.add("When purchasing the item " + itemName + " with a discount, it cannot be shipped to the specified sending address.");
                break;
        }
        return errorsList;
    }
}
