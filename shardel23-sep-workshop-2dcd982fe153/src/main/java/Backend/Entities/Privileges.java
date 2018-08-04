package Backend.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Privileges implements Serializable {

    @Id
    @GeneratedValue
    private int p_id;
    private boolean canAddItemToStore;
    private boolean canRemoveItemFromStore;
    private boolean canEditItemPrice;
    private boolean canEditItemStock;
    private boolean canEditItemIsOnSale;
    private boolean canAddItemDiscount;
    private boolean canRemoveItemDiscount;
    private boolean canAddOwner;
    private boolean canAddManager;
    private boolean canGetPurchaseHistory;
    private boolean canRemoveSaleItemFromStore;
    private boolean canRemoveCategoryDiscount;
    private boolean canEditDiscountForItem;
    private boolean canCloseStore;
    private boolean canEditCategoryDiscountForItem;

    public Privileges() {
        canAddItemToStore = true;
        canRemoveItemFromStore = true;
        canEditItemPrice = true;
        canEditItemStock = true;
        canEditItemIsOnSale = true;
        canAddItemDiscount = true;
        canRemoveItemDiscount = true;
        canAddOwner = true;
        canAddManager = true;
        canGetPurchaseHistory = true;
        canRemoveSaleItemFromStore = true;
        canRemoveCategoryDiscount = true;
        canEditDiscountForItem = true;
        canCloseStore = true;
    }

    public Privileges(boolean isOwner) {
        if (isOwner) {
            canAddItemToStore = true;
            canRemoveItemFromStore = true;
            canEditItemPrice = true;
            canEditItemStock = true;
            canEditItemIsOnSale = true;
            canAddItemDiscount = true;
            canRemoveItemDiscount = true;
            canAddOwner = true;
            canAddManager = true;
            canGetPurchaseHistory = true;
            canRemoveSaleItemFromStore= true;
            canRemoveCategoryDiscount= true;
            canEditDiscountForItem = true;
            canCloseStore= true;
            canEditCategoryDiscountForItem = true;
        } else {
            //regular privliges for manager
        }

    }

    public Privileges(boolean[] privileges) {
        canAddItemToStore = privileges[0];
        canRemoveItemFromStore = privileges[1];
        canEditItemPrice = privileges[2];
        canEditItemStock = privileges[3];
        canEditItemIsOnSale = privileges[4];
        canAddItemDiscount = privileges[5];
        canRemoveItemDiscount = privileges[6];
        canAddOwner = false;
        canAddManager = privileges[7];
        canGetPurchaseHistory = privileges[8];
        //TODO: ADD!
        //canRemoveSaleItemFromStore = privileges[9];
        //canRemoveCategoryDiscount = privileges[10];
        //canEditDiscountForItem = privileges[11];
        //canCloseStore = privileges[12];
        //canEditCategoryDiscountForItem = privileges[13];
    }

    public boolean canAddItemToStore() {
        return canAddItemToStore;
    }

    public boolean canRemoveItemFromStore() {
        return canRemoveItemFromStore;
    }

    public boolean canEditItemPrice() {
        return canEditItemPrice;
    }

    public boolean canEditItemStock() {
        return canEditItemStock;
    }

    public boolean canEditItemIsOnSale() {
        return canEditItemIsOnSale;
    }

    public boolean canAddItemDiscount() {
        return canAddItemDiscount;
    }

    public boolean canRemoveItemDiscount() {
        return canRemoveItemDiscount;
    }

    public boolean canAddOwner() {
        return canAddOwner;
    }

    public boolean canAddManager() {
        return canAddManager;
    }

    public boolean canGetPurchaseHistory() {
        return canGetPurchaseHistory;
    }

    public boolean canRemoveSaleItemFromStore() {
        return canRemoveSaleItemFromStore;
    }

    public boolean canRemoveCategoryDiscount() {
        return canRemoveCategoryDiscount;
    }

    public boolean canEditDiscountForItem() {
        return canEditDiscountForItem;
    }

    public boolean canCloseStore() {
        return canCloseStore;
    }

    public boolean canEditCategoryDiscountForItem() {
        return canEditCategoryDiscountForItem;
    }

    public boolean[] getPrivilegesAsBooleanArrForManager() {
        return new boolean[]{canAddItemToStore ,canRemoveItemFromStore ,
                canEditItemPrice ,canEditItemStock,canEditItemIsOnSale ,canAddItemDiscount,
                canRemoveItemDiscount,canAddManager ,canGetPurchaseHistory};
    }
    public boolean[] getPrivilegesAsBooleanArrForOwner() {
        return new boolean[]{canAddItemToStore ,canRemoveItemFromStore ,
                canEditItemPrice ,canEditItemStock,canEditItemIsOnSale ,canAddItemDiscount,
                canRemoveItemDiscount,canAddManager ,canGetPurchaseHistory,canRemoveSaleItemFromStore,
                canRemoveCategoryDiscount,canEditDiscountForItem,canCloseStore,canEditCategoryDiscountForItem
        };
    }
}