package Backend.Entities.Policies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ErrorList {

    @Id
    @GeneratedValue
    private int el_id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> errors;

    public ErrorList(List<String> errors) {
        this.errors = errors;
    }

    public ErrorList() {
        errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void add(String error) {
        errors.add(error);
    }

    public void remove(String error) {
        errors.remove(error);
    }

    public String toString(){
        String ans="";
        for (String string: errors) {
            ans = ans + " " + string;
        }
        return ans;
    }
}
