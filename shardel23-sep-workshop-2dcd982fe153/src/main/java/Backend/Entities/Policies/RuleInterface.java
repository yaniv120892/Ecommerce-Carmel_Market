package Backend.Entities.Policies;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public interface RuleInterface {

    @Id
    @GeneratedValue
    int getRuleId();
    void setRuleId(int id);

    ArrayList<String> calculate(int quantity, String SendingAddress, String itemName) throws Exception;
}
