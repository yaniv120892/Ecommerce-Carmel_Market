package Backend.Data;

import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Backend.Entities.Store;
import Backend.Entities.Users.User;
import Exceptions.NoSuchStoreException;

import java.util.List;
import java.util.Map;

public interface DBInterface {

    //USER FUNCS

    boolean addUser(User user);

    boolean isUserExists(String mail);

    boolean addStore(Store store);

    boolean addItem(Item item);

    Store getStoreByName(String name) throws NoSuchStoreException;

    List<Store> getAllStores();

    User checkUserPassword(String email, String hashedPW) throws Exception;

    User getUser(String userEmail) throws Exception;

    void removeUser(String toDeleteUserEmail);

    List<User> getAllUsers();

    List<Item> getAllItems();

    List<SaleItem> getAllSaleItems();

    Map<CategoryType, Discounts> getCategoryDiscountsByStoreName(String selectedStoreName) throws NoSuchStoreException;

    boolean updateUser(User user);

    boolean updateStore(Store store);
}
