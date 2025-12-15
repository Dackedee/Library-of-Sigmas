import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        loadData();
        SwingUtilities.invokeLater(() -> new Main().createLoginView());

    }

    public static void loadData() throws FileNotFoundException {

        String filePath = "src/books_the_library_system.txt";

        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\| ");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                System.out.println("Key: " + keyValue[0].trim() + ", Value: " + keyValue[1].trim());
            }
        }
    }

    private void createLoginView() {
        JFrame frame = new JFrame("Login Window");
        frame.setSize(600, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Log In");

        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));

        panel.add(centerComponent(new JLabel("Username:")));
        panel.add(centerComponent(usernameField));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(centerComponent(new JLabel("Password:")));
        panel.add(centerComponent(passwordField));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(centerComponent(loginButton));

        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isCorrectLogin(username, password)) {
                frame.getContentPane().removeAll();
                createSearchView(frame);
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private String getValue(String part) {
        return part.split(":")[1].trim();
    }

    private ArrayList<User> loadUsersData() {
        ArrayList<User> users = new ArrayList<User>();

        String filePath = "src/UsersData.txt";

        File file = new File(filePath);

        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\| ");

            for (int i = 0; i < parts.length; i++) {
                // If current value is username, create new User instance
                if (i % 2 == 0) {
                    // Value is username
                    User newUser = new User(getValue(parts[i]), getValue(parts[i + 1]));
                    users.add(newUser);
                }
            }
        }

        return users;
    }

    private boolean isCorrectLogin(String username, String password) {

        System.out.println("Checking login for user '" + username + "' with password '" + password + "'");

        ArrayList<User> users = loadUsersData();

        // Find user in users
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u.getPassword().equals(password);
            }
        }

        return false;
    }

    private void createSearchView(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top search bar
        JPanel topPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Center scrollable area for the 3 boxes
        JPanel boxesContainer = new JPanel();
        boxesContainer.setLayout(new BoxLayout(boxesContainer, BoxLayout.Y_AXIS));

        // Add three titled boxes
        boxesContainer.add(createSampleBox("Results Box 1"));
        boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        boxesContainer.add(createSampleBox("Results Box 2"));
        boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        boxesContainer.add(createSampleBox("Results Box 3"));

        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
    }

    private JPanel createSampleBox(String title) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createTitledBorder(title));

        boxPanel.add(new JLabel("Sample Line 1 Sample Line 1 Sample Line 1 Sample Line 1" ));
        boxPanel.add(new JLabel("Sample Line 2"));
        boxPanel.add(new JLabel("Sample Line 3"));
        boxPanel.add(new JLabel("Sample Line 4"));
        boxPanel.add(new JLabel("Sample Line 5"));

        return boxPanel;
    }

    // Utility to horizontally center components
    private Component centerComponent(Component comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(comp);
        return wrapper;
    }
}