import javax.swing.*;
import java.awt.*;

public class Main {

    User activeUser = null;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createStartView());
    }

    private void createStartView() {
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
        JButton registerButton = new JButton("Register");

        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));

        panel.add(centerComponent(new JLabel("Username:")));
        panel.add(centerComponent(usernameField));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(centerComponent(new JLabel("Password:")));
        panel.add(centerComponent(passwordField));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(centerComponent(loginButton));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(centerComponent(registerButton));

        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (UserManager.existsUsername(username) == true) {
                JOptionPane.showMessageDialog(frame, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    /// add new user
                    this.activeUser = UserManager.createUser(username, password);
                    JOptionPane.showMessageDialog(frame, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    createSearchView(frame);
                    frame.revalidate();
                    frame.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Logged-in user or null if incorrect
            User loginUser = UserManager.findUser(username, password);

            if (loginUser == null) {
                JOptionPane.showMessageDialog(frame, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                activeUser = loginUser;
                createSearchView(frame);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    private void createSearchView(JFrame frame) {
        System.out.println("Creating search view...");

        frame.getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top search bar
        JPanel topPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JButton myLoansButton = new JButton("My loans");
        topPanel.add(myLoansButton);

        // Center scrollable area for the 3 boxes
        JPanel boxesContainer = new JPanel();
        boxesContainer.setLayout(new BoxLayout(boxesContainer, BoxLayout.Y_AXIS));

        // Add boxes for each book in collection
        BookCollection collection = FileManager.loadBooksData();
        FileManager.loadLoanedBooksData(collection);

        for (User user : UserManager.getUsers()) {
            System.out.println("User: " + user.getUsername() + " has loaned books:");
            for (Book book : user.loanedBooks.getBooks()) {
                System.out.println(" - " + book.getTitle());
            }
        }

        // FileManager.matchLoanedBooksToUsers(loanedBooks, collection);
        for (Book book : collection.getBooks()) {
            boxesContainer.add(createBookBox(book));
            boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            AhoCorasick seachMotor = new AhoCorasick(collection);

            BookCollection results = seachMotor.search(searchTerm);

            boxesContainer.removeAll();

            for (Book book : results.getBooks()) {
                boxesContainer.add(createBookBox(book));
                boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }

            boxesContainer.revalidate();
            boxesContainer.repaint();
        });

        myLoansButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createLoanView(frame);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void createLoanView(JFrame frame){
        JPanel loanPanel = new JPanel();
        loanPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel titleText = new JLabel();
        titleText.setText("MY LOANS");

        headerPanel.add(titleText);
        JButton homePageButton = new JButton("Home");
        headerPanel.add(homePageButton);

        JPanel boxesContainer = new JPanel();
        boxesContainer.setLayout(new BoxLayout(boxesContainer, BoxLayout.Y_AXIS));

        BookCollection loanedBooks = FileManager.getUserLoanedBooksData(activeUser);
        for (Book book : loanedBooks.getBooks()) {
        for (Book book : activeUser.loanedBooks.getBooks()) {
            boxesContainer.add(createBookBox(book));
            boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        loanPanel.add(headerPanel, BorderLayout.NORTH);
        loanPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(loanPanel);

        homePageButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createSearchView(frame);
            frame.revalidate();
            frame.repaint();
        });
    }

    private JPanel createBookBox(Book book) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createTitledBorder(book.getTitle()));

        boxPanel.add(new JLabel("Author: " + book.getAuthor()));
        boxPanel.add(new JLabel("ISBN: " + book.getISBN()));
        boxPanel.add(new JLabel("Pages: " + book.getPages()));
        boxPanel.add(new JLabel("Language: " + book.getLanguage()));
        boxPanel.add(new JLabel("Year: " + book.getYear()));

        JLabel availableLabel = new JLabel("Available Copies: " + book.getAmountAvailable() + "/" + book.getTotalAmount());
        boxPanel.add(availableLabel);

        JButton borrowButton = new JButton("Borrow");
        boxPanel.add(borrowButton);

        JButton returnButton = new JButton("Return");
        boxPanel.add(returnButton);

        borrowButton.addActionListener(e -> {
            try {
                book.checkOut(activeUser);
                availableLabel.setText("Available Copies: " + book.getAmountAvailable() + "/" + book.getTotalAmount());
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(new JFrame(), "No copies available to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            try {
                book.returnBook(activeUser);
                availableLabel.setText("Available Copies: " + book.getAmountAvailable() + "/" + book.getTotalAmount());
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(new JFrame(), "No book to return.");
            }
        });

        return boxPanel;
    }

    /* private JPanel createSampleBox(String title) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createTitledBorder(title));

        boxPanel.add(new JLabel("Sample Line 1 Sample Line 1 Sample Line 1 Sample Line 1" ));
        boxPanel.add(new JLabel("Sample Line 2"));
        boxPanel.add(new JLabel("Sample Line 3"));
        boxPanel.add(new JLabel("Sample Line 4"));
        boxPanel.add(new JLabel("Sample Line 5"));

        return boxPanel;
    } */

    // Utility to horizontally center components
    private Component centerComponent(Component comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(comp);
        return wrapper;
    }
}