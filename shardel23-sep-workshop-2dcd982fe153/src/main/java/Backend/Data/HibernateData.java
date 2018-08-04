package Backend.Data;

import Backend.Addons.Security;
import Backend.Entities.Cart;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HibernateData implements DBInterface {

    public HibernateData() {
        //populateDB();
    }

    private void populateDB() {
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
        try {
            store1.addOwner(null, user2);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        HibernateUtil.saveObject(user);
        return true;
    }

    @Override
    public boolean addItem(Item item) {
        HibernateUtil.saveObject(item);
        return true;
    }

    @Override
    public boolean isUserExists(String mail) {
        ArrayList<User> list = (ArrayList<User>)getAllUsers();
        for (User user : list) {
            if (user.getEmail().equals(mail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addStore(Store store) {
        HibernateUtil.saveObject(store);
        return true;
    }

    @Override
    public Store getStoreByName(String name) throws NoSuchStoreException {
        List<Store> stores = getAllStores();
        for (Store store : stores) {
            if (store.getName().equals(name)) {
                return store;
            }
        }
        throw new NoSuchStoreException("NO SUCH STORE");
    }

    @Override
    public List<Store> getAllStores() {
        return (List<Store>)HibernateUtil.getObjects("Store");
    }

    @Override
    public User checkUserPassword(String email, String hashedPW) throws Exception {
        User user = getUser(email);
        if (user.getHashedPW().equals(hashedPW)) {
            return user;
        }
        return null;
    }

    @Override
    public User getUser(String userEmail) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail().equals(userEmail)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void removeUser(String toDeleteUserEmail) {
        User user = getUser(toDeleteUserEmail);
        HibernateUtil.deleteObject(user);
    }

    @Override
    public boolean updateUser(User user) {
        try {
            Cart cart = user.getCart();
            /*
            Set<CartItem> cartItems = cart.getItems();
            for (CartItem cartItem : cartItems) {
                HibernateUtil.updateObject(cartItem);
            }
            */
            //HibernateUtil.updateObject(cart);
            HibernateUtil.updateObject(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateStore(Store store) {
        try {
            HibernateUtil.updateObject(store);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>)HibernateUtil.getObjects("User");
    }

    @Override
    public List<Item> getAllItems() {
        return (List<Item>)HibernateUtil.getObjects("Item");
    }

    @Override
    public List<SaleItem> getAllSaleItems() {
        return (List<SaleItem>)HibernateUtil.getObjects("SaleItem");
    }

    @Override
    public Map<CategoryType, Discounts> getCategoryDiscountsByStoreName(String selectedStoreName) throws NoSuchStoreException {
        Store store = getStoreByName(selectedStoreName);
        return store.getCategoryDiscounts();
    }
}
