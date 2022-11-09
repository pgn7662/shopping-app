package databases;

import Models.Customer;
import Models.User;
import repositories.UserRepository;

public class UserDatabase implements UserRepository {
    private static UserDatabase userDatabase;

    private UserDatabase() {
    }
    
    public static UserDatabase getInstance() {
        if (userDatabase == null)
            userDatabase = new UserDatabase();
        return userDatabase;
    }

    @Override
    public boolean isUserEmailPresent(String email) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public User getUser(String email, String password) {
        // TODO Auto-generated method stub
        return new Customer(email, email, password, 0, null);
    }

    @Override
    public User addUser(Customer customer) {
        // TODO
        return customer;
    }

    @Override
    public boolean updateUser(User user) {
        // TODO Auto-generated method stub
        return true;
    }
}
