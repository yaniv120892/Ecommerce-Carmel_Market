package Backend.Entities.Policies;

import Backend.Entities.Enums.LogicOperator;
import Exceptions.BadArgumentException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.ArrayList;

@Entity
public class CompositeRule implements RuleInterface{

    private int ruleId;
    @Id
    @GeneratedValue
    private int comp_id;
    @OneToOne
    private RuleInterface leftChild;
    @OneToOne
    private RuleInterface rightChild;
    private LogicOperator operator;

    public CompositeRule() {
    }

    public RuleInterface getLeftChild() {
        return leftChild;
    }

    public RuleInterface getRightChild() {
        return rightChild;
    }

    public LogicOperator getOperator() {
        return operator;
    }

    public CompositeRule(RuleInterface leftChild, RuleInterface rightChild, LogicOperator operator) throws Exception{
        if(rightChild == null) throw new BadArgumentException("right child is null");
        if(leftChild == null && operator != LogicOperator.NOT) throw new BadArgumentException("left child is null and the operator isn't NOT");
        if(leftChild != null && operator == LogicOperator.NOT) throw new BadArgumentException("left child is not null and the operator is NOT");
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.operator = operator;
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
        ArrayList<String> errorsList = new ArrayList<>();
        ArrayList<String> rightChildErrors = rightChild.calculate(quantity, sendingAddress, itemName);
        ArrayList<String> leftChildErrors = new ArrayList<>();
        if(leftChild != null)
            leftChildErrors = leftChild.calculate(quantity, sendingAddress, itemName);
        switch (operator){
            case AND:
                if(leftChildErrors.size() != 0 || rightChildErrors.size() != 0) {
                    errorsList.addAll(leftChildErrors);
                    errorsList.addAll(rightChildErrors);
                }
                break;
            case OR:
                if(leftChildErrors.size() != 0 && rightChildErrors.size() != 0) {
                    errorsList.addAll(leftChildErrors);
                    errorsList.addAll(rightChildErrors);
                }
                break;
            case NOT:
                if(rightChildErrors.size() == 0)
                    errorsList.add("Not all of the rules in the purchase policy of the item " + itemName + " are fixed.");
                break;
        }
        return errorsList;
    }
}
