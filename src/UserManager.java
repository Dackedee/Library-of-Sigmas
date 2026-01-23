import java.util.ArrayList;

public class UserManager {
    ArrayList<User> users;
    FileManager fileManager = new FileManager();
    public UserManager() {
        this.users = this.fileManager.loadUsersData();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }
    public User createUser(String username, String password) {
        String id = (this.users.size() + 1) + "";
        User newUser = new User(username, password, id);
        this.users.add(newUser);
        return newUser;
    }
    public void removeUser(User user) {
        this.users.remove(user);
    }
    public Boolean existsUsername(String username) {
        for (User u : this.users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    public User findUser(String username, String password) {
        for (User u : this.users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

}
