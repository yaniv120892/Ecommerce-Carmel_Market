package Backend.Entities.Policies;

import Backend.Entities.Cart;
import Backend.Entities.Enums.LogicOperator;
import Backend.Entities.Enums.PurchasePolicyType;
import Backend.Entities.Enums.RuleType;
import Backend.Entities.Items.CartItem;
import Exceptions.BadArgumentException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class PurchasePolicy {

    @Id
    @GeneratedValue
    private int pp_id;
    private PurchasePolicyType PPT;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<String, RuleInterface> rulePerItem;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<String, ErrorList> literalPP;

    public PurchasePolicy() {
    }

    public Map<String, RuleInterface> getRulePerItem() {
        return rulePerItem;
    }

    public PurchasePolicy(PurchasePolicyType PPT) {
        this.PPT = PPT;
        this.rulePerItem = new HashMap<>();
        this.literalPP = new HashMap<>();
    }

    public void updateLiteralPP(String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail){
        if(!literalPP.containsKey(itemName))
            literalPP.put(itemName, new ErrorList());
        List<String> literalRuleTree = literalPP.get(itemName).getErrors();
        String newRule = "";
        if(literalRuleTree.size() == 0 && operator != "NOT"){
            newRule = "Not ";
        }
        else{
            if(!literalRuleTree.equals("") && operator.equals("AND"))
                newRule = "And, must ";
            else if(!literalRuleTree.equals("") && operator.equals("OR"))
                newRule = "Or, must ";
            if(notBit)
                newRule = newRule + "not ";
        }
        switch (RuleType.valueOf(ruleType)){
            case MAX_AMOUNT:
                newRule = newRule + "purchase an amount of " + numberDetail + " or below,";
                break;
            case MIN_AMOUNT:
                newRule = newRule + "purchase an amount of " + numberDetail + " or above,";
                break;
            case ALLOWED_SENDING_ADDRESS:
                newRule = newRule + "type the sending address of " + stringDetail + ",";
                break;
            case ALLOWED_DISCOUNT_SENDING_ADDRESS:
                newRule = newRule + "type the sending address of " + stringDetail + "in order to get a discount,";
        }
        ErrorList errorList = new ErrorList();
        errorList.add(literalRuleTree + "<br />" + newRule);
        literalPP.put(itemName, errorList);
    }

    public void addRule(String itemName, boolean notBit, String operator, String ruleType, Integer numberDetail, String stringDetail) throws Exception{
        if(operator != null && !operator.equals("AND") && !operator.equals("OR"))
            throw new BadArgumentException("unfamiliar operator");
        if(!ruleType.equals("MAX_AMOUNT") && !ruleType.equals("MIN_AMOUNT") && !ruleType.equals("ALLOWED_SENDING_ADDRESS") && !ruleType.equals("ALLOWED_DISCOUNT_SENDING_ADDRESS"))
            throw new BadArgumentException("unfamiliar rule type");
        if((ruleType == "MAX_AMOUNT" || ruleType == "MIN_AMOUNT") && numberDetail == null)
            throw new BadArgumentException("numberDetail is null when it is needed by the rule type");
        if((ruleType == "ALLOWED_SENDING_ADDRESS" || ruleType == "ALLOWED_DISCOUNT_SENDING_ADDRESS") && stringDetail == null)
            throw new BadArgumentException("stringDetail is null when it is needed by the rule type");

        Rule toAdd = new Rule(RuleType.valueOf(ruleType), numberDetail, stringDetail);
        if(!rulePerItem.containsKey(itemName)){
            if(notBit && !rulePerItem.containsKey(itemName)){
                CompositeRule newCompositeRule = new CompositeRule(null, toAdd, LogicOperator.NOT);
                rulePerItem.put(itemName, newCompositeRule);
            }
            else
                rulePerItem.put(itemName, toAdd);
        }
        else{
            if(operator == null)
                throw new BadArgumentException("- operator, the item already have a rule but the operator is null");
            LogicOperator op = LogicOperator.valueOf(operator);
            RuleInterface oldRule = rulePerItem.get(itemName);
            CompositeRule finalCompositeRule;
            if(notBit){
                CompositeRule newCompositeRule = new CompositeRule(null, toAdd, LogicOperator.NOT);
                finalCompositeRule = new CompositeRule(oldRule, newCompositeRule, op);
            }
            else
                finalCompositeRule = new CompositeRule(oldRule, toAdd, op);
            rulePerItem.put(itemName, finalCompositeRule);
        }
        updateLiteralPP(itemName, notBit, operator, ruleType, numberDetail, stringDetail);
    }

    public void removeRule(String itemName) throws Exception{
        if(!rulePerItem.containsKey(itemName))
            throw new BadArgumentException("the item" + itemName + " doesn't have any rule");
        rulePerItem.remove(itemName);
        literalPP.remove(itemName);
    }

    //clarification: the itemName "" mean "for all items".
    public ArrayList<String> validateItem(String itemName, int quantity, String sendingAddress) throws Exception{
        if(quantity <= 0) throw new BadArgumentException("the quantity is <= 0");
        ArrayList<String> errorsList = new ArrayList<>();
        if(rulePerItem.containsKey("")){
            if(rulePerItem.get("").calculate(quantity, sendingAddress, itemName).size() == 0){
                if (rulePerItem.containsKey(itemName))
                    return rulePerItem.get(itemName).calculate(quantity, sendingAddress, itemName);
            }
            else if(rulePerItem.containsKey(itemName)){
                errorsList.addAll(rulePerItem.get("").calculate(quantity, sendingAddress, itemName));
                errorsList.addAll(rulePerItem.get(itemName).calculate(quantity, sendingAddress, itemName));
                return errorsList;
            }
            else
                return rulePerItem.get("").calculate(quantity, sendingAddress, itemName);
        }
        else if(rulePerItem.containsKey(itemName))
            errorsList.addAll(rulePerItem.get(itemName).calculate(quantity, sendingAddress, itemName));
        return errorsList;
    }

    public ArrayList<String> validateCart(Cart cart, String sendingAddress) throws Exception{
        ArrayList<String> errorsList = new ArrayList<>();
        for(CartItem cartItem : cart.getItems()){
            errorsList.addAll(validateItem(cartItem.getItem().getName(), cartItem.getAmount(), sendingAddress));
        }
        return errorsList;
    }


    public void removeLiteralPP(String itemName) {
        literalPP.remove(itemName);
    }

    public String getLiteralPP(String itemName) throws Exception{
        return literalPP.get(itemName).toString();
    }
}
