package Backend.Entities;

import Backend.Addons.EventLogger;
import Backend.Addons.TreeNode.TreeNode;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Discount.HiddenDiscount;
import Backend.Entities.Discount.VisibleDiscount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.DiscountType;
import Backend.Entities.Enums.PurchasePolicyType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.RaffleItem;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Items.StoreItem;
import Backend.Entities.Policies.PurchasePolicy;
import Backend.Entities.Users.User;
import Exceptions.*;
import javafx.util.Pair;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "stores")
public class Store {

    private String storeEmail;
    @Id
    private final String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<StoreItem> storeItems;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SaleItem> saleItems;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PurchaseHistory> purchaseHistory;
    @OneToOne(cascade = CascadeType.ALL)
    private PurchasePolicy purchasePolicy;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<CategoryType, PurchasePolicy> purchasePolicyPerCategoryInStore;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> storeOwners;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<CategoryType, Discounts> categoryDiscounts;
    private boolean isActive;
    @Transient
    private TreeNode<User> tree;

    public Store(String newStoreName, String storeEmail) {
        this.name = newStoreName;
        this.storeEmail = storeEmail;
        purchaseHistory = new HashSet<>();
        storeItems = new HashSet<>();
        saleItems = new HashSet<>();
        isActive = true;
        storeOwners = new HashSet<>();
        this.categoryDiscounts = new HashMap<CategoryType, Discounts>();
        purchasePolicy = new PurchasePolicy(PurchasePolicyType.STORE);
        purchasePolicyPerCategoryInStore = new HashMap<>();
        for (CategoryType ct : CategoryType.values())
            purchasePolicyPerCategoryInStore.put(ct, new PurchasePolicy(PurchasePolicyType.CATEGORY_IN_STORE));

    }

    public Store() {
        name = "";
    }


    public String getName() {
        return name;
    }

    public Set<SaleItem> getStoreSaleItems() {
        return saleItems;
    }

    public ArrayList<Item> getStoreItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (StoreItem storeItem : storeItems) {
            if (storeItem.isOnSale() && storeItem.getAmount() > 0) {
                items.add(storeItem.getItem());
            }
        }
        return items;
    }

    public Item getStoreItem(String itemName) {
        for (StoreItem storeItem : storeItems) {
            Item item = storeItem.getItem();
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void addItemToStore(String itemName, int initialAmountInStock, double price, String category) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                throw new NotPremittedException("NAME ALREADY TAKEN!");
            }
        }
        if (initialAmountInStock < 0) {
            throw new BadArgumentException("AMOUNT IN STOCK MUST BE NON NEGATIVE");
        }
        if (price <= 0) {
            throw new BadArgumentException("PRICE MUST BE POSITIVE");
        }
        Item item = new Item(itemName, this, price, CategoryType.valueOf(category));

        storeItems.add(new StoreItem(true, initialAmountInStock, item));
    }

    public void addItemToSale(String itemName, String saleType) throws NotPremittedException {
        if (isSaleItemAlreadyExists(itemName, saleType)) {
            throw new NotPremittedException("SaleItem for item " + itemName + " with saletype " + saleType + " already exists");
        }
        Item item = getStoreItem(itemName);
        if (SaleType.valueOf(saleType).equals(SaleType.NORMAL)) {
            saleItems.add(new SaleItem(item));
            return;
        }
        throw new NotPremittedException("NOT A NORMAL SALE!");
    }


    public void addItemToSale(String itemName, String saleType, String startDate, String endDate) throws NotPremittedException, ParseException {
        if (isSaleItemAlreadyExists(itemName, saleType)) {
            throw new NotPremittedException("SaleItem for item " + itemName + " with saletype " + saleType + " already exists");
        }
        Item item = getStoreItem(itemName);
        if (SaleType.valueOf(saleType).equals(SaleType.RAFFLE)) {
            saleItems.add(new RaffleItem(item, startDate, endDate));
            return;
        }
        throw new NotPremittedException("NOT A RAFFLE SALE!");
    }

    private boolean isSaleItemAlreadyExists(String itemName, String saleType) {
        for (SaleItem item : saleItems) {
            if (item.getItem().getName().equals(itemName) && item.getType().equals(SaleType.valueOf(saleType))) {
                return true;
            }
        }
        return false;
    }


    public void removeItemFromStore(String itemName) throws Exception {
        ArrayList<SaleItem> toRemove = new ArrayList<>();
        for (SaleItem item : saleItems) {
            if (item.getItem().getName().equals(itemName)) {
                toRemove.add(item);
            }
        }
        for (SaleItem item : toRemove) {
            saleItems.remove(item);
        }

        for (SaleItem item : saleItems) {
            if (item.getItem().getName().equals(itemName)) {
                saleItems.remove(item);
                break;
            }
        }

        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItems.remove(storeItem);
                return;
                //TODO: Check if we can do this or get modification exception
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public void editItemPrice(String itemName, double newPrice) throws Exception {
        if (newPrice <= 0) {
            throw new BadArgumentException("PRICE MUST BE POSITIVE");
        }
        for (StoreItem storeItem  : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.getItem().changePrice(newPrice);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public int getItemStock(String itemName) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                return storeItem.getAmount();
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public void editItemStock(String itemName, int newAmount) throws Exception {
        if (newAmount < 0) {
            throw new BadArgumentException("AMOUNT IN STOCK MUST BE NON NEGATIVE");
        }
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.setAmount(newAmount);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public void editItemIsOnSale(String itemName, boolean newValue) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.setOnSale(newValue);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }


    public void addCategoryDiscount(String categoryName, String discountType, String startDate, String endDate, double percentOff) throws Exception{
        CategoryType currentType = CategoryType.valueOf(categoryName);
        Discount discount;
        if (!this.categoryDiscounts.containsKey(currentType)) {
            this.categoryDiscounts.put(currentType, new Discounts());
        }
        switch (DiscountType.valueOf(discountType)) {
            case VISIBLE:
                discount = new VisibleDiscount(startDate, endDate, percentOff);
                break;
            case HIDDEN:
                discount = new HiddenDiscount(startDate, endDate, percentOff);
                ((HiddenDiscount) discount).generateCouponCode();
                break;
            default:
                throw new NoSuchDiscountException("discount " + discountType + " is not a valid discount type");
        }
        this.categoryDiscounts.get(currentType).add(discount);
    }

    public void removeCategoryDiscount(String categoryName, String discountType, String startDate, String endDate) throws Exception {
        CategoryType currentType = CategoryType.valueOf(categoryName);
        if (!this.categoryDiscounts.containsKey(currentType)) {
            return;
        }
        DiscountType discountTypeObject = DiscountType.valueOf(discountType);
        for (Discount discount : categoryDiscounts.get(currentType).getDiscountList()) {
            if (discount.isSameDiscount(discountTypeObject, startDate, endDate)) {
                this.categoryDiscounts.get(currentType).remove(discount);
                return;
            }
        }
    }

    public void addItemDiscount(String itemName, String discountType, String startDate, String endDate, double percentOff) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.getItem().addDiscount(discountType, startDate, endDate, percentOff);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public void removeItemDiscount(String itemName, String discountType, String startDate, String endDate) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.getItem().removeDiscount(discountType, startDate, endDate);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public PurchasePolicy getStorePurchasePolicy() {
        return purchasePolicy;
    }

    public ArrayList<Item> getStoreItemsInCategory(String category) {
        ArrayList<Item> itemsInCategory = new ArrayList<>();
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getCategory().equals(CategoryType.valueOf(category))) {
                itemsInCategory.add(storeItem.getItem());
                //TODO: Check if we can do this or get modification exception
            }
        }

        return itemsInCategory;
    }

    public Set<PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public double checkout(User user, Item item, Integer numberToBuy, SaleType saleType, double price, Set<Pair<String, String>> messages) throws Exception {
        boolean foundItem = false;
        for (SaleItem saleItem : saleItems) {
            if (saleItem.getItem().equals(item) && saleItem.getType().equals(saleType)) {
                saleItem.checkout(user, numberToBuy, messages);
                foundItem = true;
                if (saleType.equals(SaleType.RAFFLE)) {
                    if (((RaffleItem) saleItem).getAlreadyBought() == 100) {
                        saleItems.remove(saleItem);
                        for (User owner : getOwners()) {
                            messages.add(new Pair<>(owner.getEmail(), "Raffle has ended for " + item.getName()));
                        }
                    }
                } else {
                    for (User owner : getOwners()) {
                        messages.add(new Pair<>(owner.getEmail(), "Someone has bought" + item.getName() + " from you"));
                    }
                }

                break;
            }
        }
        if (foundItem) {
            price = addCategoryDiscount(user, item, price);
            this.purchaseHistory.add(new PurchaseHistory(item.getName(), user.getEmail(), this.name, numberToBuy, price, ""));
            return price;
        }
        throw new NoSuchItemException("Sale Item was not found in the store");
    }

    private double addCategoryDiscount(User user, Item item, double price) {
        for (CategoryType categoryType : this.categoryDiscounts.keySet()) {
            if (item.getCategory().equals(categoryType)) {
                for (Discount discount : this.categoryDiscounts.get(categoryType).getDiscountList()) {
                    price = price - price * (discount.getDiscountPercent(user.getUserCoupons()) / 100);
                }
            }
        }
        return price;
    }

    public void rollback(User user, Item item, int numberToBuy, SaleType saleType, double price) throws Exception {
        boolean foundItem = false;
        for (SaleItem saleItem : saleItems) {
            if (saleItem.getItem().equals(item) && saleItem.getType().equals(saleType)) {
                saleItem.rollback(user, numberToBuy);
                foundItem = true;
                break;
            }
        }
        if (foundItem) {
            this.purchaseHistory.remove(this.purchaseHistory.size() - 1);
            return;
        }
        throw new NoSuchItemException("Sale Item was not found in the store for rollback!");
    }

    public void addOwner(User owner, User user) throws Exception {
        storeOwners.add(user);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        //System.out.println(dtf.format(now)); //2016/11/16 12:08:43
        if (owner == null) {
            //first owner
            tree = new TreeNode<>(user, dtf.format(now), user.getStorePrivileges(this.name));
        } else {
            TreeNode<User> node = tree.findTreeNode(owner);
            node.addChild(user, dtf.format(now), user.getStorePrivileges(this.name));
        }
    }

    public void addManager(User adder, User beingAdded) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        TreeNode<User> node = tree.findTreeNode(adder);
        node.addChild(beingAdded, dtf.format(now), beingAdded.getStorePrivileges(this.name));
    }

    public Set<User> getOwners() {
        return storeOwners;
    }

    public void removeOwner(String toDeleteUserEmail) {
        for (User user : getOwners()) {
            if (user.getEmail().equals(toDeleteUserEmail)) {
                getOwners().remove(user);
                return;
            }
        }
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void addSaleItemsByName(String itemName, ArrayList<SaleItem> items) {
        for (SaleItem item : saleItems) {
            if (item.getItem().getName().equals(itemName)) {
                items.add(item);
            }
        }
    }

    public void addSaleItemsByCategory(CategoryType category, Set<SaleItem> items) {
        for (SaleItem item : saleItems) {
            if (item.getItem().getCategory().equals(category)) {
                items.add(item);
            }
        }
    }

    public void removeSaleItemFromStore(String itemName, String saleType) {
        for (SaleItem item : saleItems) {
            if (item.getItem().getName().equals(itemName) && item.getType().equals(SaleType.valueOf(saleType))) {
                saleItems.remove(item);
                return;
            }
        }
    }

    public Map<CategoryType, Discounts> getCategoryDiscounts() {
        return categoryDiscounts;
    }

    public Set<Discount> getItemDiscounts(String item) throws NoSuchItemException {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(item)) {
                return storeItem.getItem().getDiscounts();
            }
        }
        throw new NoSuchItemException("item does not exist");
    }

    public PurchasePolicy getCategoryPurchasePolicy(String categoryName) {
        return purchasePolicyPerCategoryInStore.get(CategoryType.valueOf(categoryName));
    }

    public void editDiscountForItem(String itemName, String newDiscountType, String newStartDate, String newEndDate,
                                    String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate,
                                    String oldPercent) throws ParseException, NoSuchItemException,Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(itemName)) {
                storeItem.getItem().editDiscountForItem(newDiscountType, newStartDate, newEndDate,
                        newPercent, oldDiscountType, oldStartDate, oldEndDate,
                        oldPercent);
                return;
            }
        }
        throw new NoSuchItemException("item does not exist");
    }

    public TreeNode<User> getTree() {
        return tree;
    }


    public void getSaleItemsByCategory(String categoryName, ArrayList<SaleItem> items) {
        for (SaleItem item : saleItems) {
            if (item.getItem().getCategory().equals(CategoryType.valueOf(categoryName))) {
                items.add(item);
            }
        }
    }

    public void editCategoryDiscount(String categoryType, String discountType, String startDate, String endDate, double percentOff) throws ParseException {
        Discounts discounts = categoryDiscounts.get(CategoryType.valueOf(categoryType));
        for (Discount discount : discounts.getDiscountList()) {
            discount.isSameDiscount(DiscountType.valueOf(discountType), startDate, endDate);
        }
    }

    public void editCategoryDiscount(String categoryName, String newDiscountType, String newStartDate, String newEndDate, String newPercent, String oldDiscountType, String oldStartDate, String oldEndDate, String oldPercent)
            throws Exception {
        CategoryType cat = CategoryType.valueOf(categoryName);
        Discounts discounts = categoryDiscounts.get(cat);
        for (Discount discount : discounts.getDiscountList()) {
            if (discount.getType().equals(DiscountType.valueOf(oldDiscountType)) && discount.isSameStartDate(oldStartDate) && discount.isSameEndDate(oldEndDate) && discount.getDiscountPercent() == Double.valueOf(oldPercent)) {
                if (!(DiscountType.valueOf(oldDiscountType).equals(DiscountType.valueOf(newDiscountType)))) {
                    this.removeCategoryDiscount(categoryName, oldDiscountType, oldStartDate, oldEndDate);
                    this.addCategoryDiscount(categoryName, newDiscountType, newStartDate, newEndDate, Double.parseDouble(newPercent));
                } else {
                    //stay in same type
                    discount.setStartDate(newStartDate);
                    discount.setEndDate(newEndDate);
                    discount.setPercent(newPercent);
                }
                break;
            }
        }
    }

    public void decrementItemStock(String name, int numberOfItems) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(name)) {
                int after = storeItem.addAndGet(numberOfItems*-1);
                if(after < 0){
                    after = storeItem.addAndGet(numberOfItems);
                    throw new CheckoutFailedException("Not enough Items, the current stock is:" + after);
                }
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }

    public void incrementItemStock(String name, int numberOfItems) throws Exception {
        for (StoreItem storeItem : storeItems) {
            if (storeItem.getItem().getName().equals(name)) {
                storeItem.addAndGet(numberOfItems);
                return;
            }
        }
        throw new NoSuchItemException("NO SUCH ITEM IN STORE");
    }
}
