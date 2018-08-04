package Exceptions;

import java.util.ArrayList;

public class PurchasePolicyException extends Exception{

    private ArrayList<String> errorsList;

    public PurchasePolicyException(ArrayList<String> errorsList){
        this.errorsList = errorsList;
    }

    public ArrayList<String> getErrorsList() {
        return errorsList;
    }
}
