package businesslogic.user;

import javafx.collections.ObservableList;

public class UserManager {
    private User currentUser;

    public void fakeLogin(String username) //TODO: bisogna implementare il login vero!
    {
        this.currentUser = User.loadUser(username);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public ObservableList<User> getAllCook(){
        return User.loadAllCook();
    }
}
