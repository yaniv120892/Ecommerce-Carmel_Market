package Backend.Data;

import Backend.Addons.EventLogger;
import Backend.Addons.Security;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.UserType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Store;
import Backend.Entities.Users.AdminUser;
import Backend.Entities.Users.RegUser;
import Backend.Entities.Users.User;
import Exceptions.NoSuchStoreException;
import Exceptions.NoSuchUserException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListData implements DBInterface {
    List<User> users;
    List<Store> stores;

    public ListData() throws Exception {
        users = Collections.synchronizedList(new ArrayList<User>());
        stores = Collections.synchronizedList(new ArrayList<Store>());

        //ArrayList<Pair<CategoryType,Backend.Entities.Discount>> ans = new ArrayList<>();
        //Backend.Entities.Discount d = new VisibleDiscount("12-08-2010","20-08-2012",15);
        //ans.add(new Pair<CategoryType,Backend.Entities.Discount>(CategoryType.BOOKS,d));

        //store
        Store store1 = new Store("Store1", "store1@gmail.com");
        try {
            store1.addItemToStore("Dark Chocolate", 30, 10.0, "CHOCOLATES");
            store1.addItemToStore("Chocolate Milk", 30, 12.0, "CHOCOLATES");
            store1.addItemToSale("Chocolate Milk", "NORMAL");
            store1.addItemToSale("Chocolate Milk", "RAFFLE", "28-11-2018", "28-12-2018");
            store1.addItemToSale("Dark Chocolate", "NORMAL");
            store1.addItemToSale("Dark Chocolate", "RAFFLE", "28-11-2018", "28-12-2018");
            store1.addCategoryDiscount("BOOKS", "VISIBLE", "12-08-2010", "20-08-2012", 15);

        } catch (Exception e) {
            e.printStackTrace();
        }
        addStore(store1);

        //admin
        User user = new AdminUser(this);
        user.setEmail("a@gmail.com");
        user.setPassword(Security.sha256string("a"));
        addUser(user);

        //user - owner of store1
        User user2 = new RegUser();
        user2.setEmail("u@gmail.com");
        user2.setPassword(Security.sha256string("u"));
        user2.setType(UserType.REGISTERED);
        try {
            user2.addStoreAsOwner(store1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addUser(user2);
        store1.addOwner(null, user2);

        //another admin
        User user3 = new AdminUser(this);
        user3.setEmail("a3@gmail.com");
        user3.setPassword(Security.sha256string("abc"));
        addUser(user3);


        //another user
        User user4 = new RegUser();
        user4.setEmail("u2@gmail.com");
        user4.setPassword(Security.sha256string("u2"));
        user4.setType(UserType.REGISTERED);
        addUser(user4);
    }

    @Override
    public boolean addUser(User user) {
        return users.add(user);
    }

    @Override
    public boolean isUserExists(String mail) {
        for (User user : users) {
            if (user.getEmail().equals(mail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addStore(Store store) {
        return stores.add(store);
    }

    @Override
    public boolean addItem(Item item) {
        return false;
    }

    @Override
    public Store getStoreByName(String name) throws NoSuchStoreException {
        for (Store store : stores) {
            if (store.getName().equals(name) && store.getIsActive()) {
                return store;
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE!");
    }


    @Override
    public List<Store> getAllStores() {
        List<Store> ans = new ArrayList<Store>();
        ans.addAll(stores);
        return ans;
    }

    @Override
    public User checkUserPassword(String email, String hashedPW) throws Exception {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getHashedPW().equals(hashedPW)) {
                return user;
            }
        }
        throw new NoSuchUserException();
    }

    @Override
    public User getUser(String userEmail) throws Exception {
        for (User user : users) {
            if (user.getEmail().equals(userEmail)) {
                return user;
            }
        }
        throw new NoSuchUserException();
    }

    @Override
    public void removeUser(String toDeleteUserEmail) {
        for (User user : users) {
            if (user.getEmail().equals(toDeleteUserEmail)) {
                users.remove(user);
                return;
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> ans = new ArrayList<>();
        for (Store store : stores) {
            if (store.getIsActive()) {
                ans.addAll(store.getStoreItems());
            }
        }
        return ans;
    }

    @Override
    public List<SaleItem> getAllSaleItems() {
        List<SaleItem> ans = new ArrayList<>();
        for (Store store : stores) {
            if (store.getIsActive()) {
                ans.addAll(store.getStoreSaleItems());
            }
        }
        return ans;
    }

    @Override
    public Map<CategoryType, Discounts> getCategoryDiscountsByStoreName(String selectedStoreName) throws NoSuchStoreException {
        Map<CategoryType, Discounts> discounts = this.getStoreByName(selectedStoreName).getCategoryDiscounts();
        ArrayList<Pair<CategoryType, Discount>> ans = new ArrayList<>();
        for (Map.Entry<CategoryType, Discounts> entry : discounts.entrySet()) {
            for (Discount discount : entry.getValue().getDiscountList()) {
                ans.add(new Pair<>(entry.getKey(), discount));
            }
        }
        return discounts;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean updateStore(Store store) {
        return false;
    }
}

