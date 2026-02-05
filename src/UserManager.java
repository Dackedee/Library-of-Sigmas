import java.util.ArrayList;

public class UserManager {
    private static ArrayList<User> users = FileManager.loadUsersData();;

    public static ArrayList<User> getUsers() {
        return users;
    }
    public static void add(User user) {
        users.add(user);
    }
    public static User create(String username, String password) {
        String id = (users.size()) + "";
        User newUser = new User(username, password, id);
        users.add(newUser);
        FileManager.createUser(username, password);
        System.out.println(newUser.getUsername() + " created with ID: " + newUser.getID());
        return newUser;
    }
    public static void remove(User user) {
        users.remove(user);
    }
    public static Boolean existsUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    public static User find(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static User find(String ID) {
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(ID)) {
                return u;
            }
        }
        return null;
    }
}
