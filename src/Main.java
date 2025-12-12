import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
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
            frame.getContentPane().removeAll();
            createSearchView(frame);
            frame.revalidate();
            frame.repaint();
        });
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