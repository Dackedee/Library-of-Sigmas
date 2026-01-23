import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {

    private static boolean activated = true;
    private static String basePath = Paths.get("").toAbsolutePath().toString() + "\\";
    private static String booksPath = basePath + "books_the_library_system.txt";
    private static String usersPath = basePath + "UsersData.txt";
    private static String loanedBooksPath = basePath + "LoanedBooks.txt";

    private static String content = null;

    public static String getusersPath() {
        return usersPath;
    }

    public String getContent() {
        return content;
    }

    public boolean isActivated() {
        return activated;
    }

    public void createFile() {

        if (!activated || usersPath == null || usersPath.isEmpty()) {
            System.out.println("FileManager is not activated or usersPath is empty.");
            return;
        }

        try {

            File file = new File(usersPath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                writeToFile(usersPath, content);
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException e) {

            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();

        }
    }

    private static String getValue(String part) {
        return part.split(":")[1].trim();
    }

    public static BookCollection loadBooksData() {

        File file = new File(booksPath);

        BookCollection collection = new BookCollection();

        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\| ");

            Book book = new Book(getValue(parts[0]), getValue(parts[1]), Integer.parseInt(getValue(parts[2])), getValue(parts[3]), Integer.parseInt(getValue(parts[4])), getValue(parts[5]), 5);
            collection.addBook(book);
        }

        return collection;
    }

    public static void matchLoanedBooksToUsers(ArrayList<User> users, BookCollection loanedBooks, BookCollection allBooks) {
        // TODO: Inget av detta fucking fungerar
        for (Book book : loanedBooks.getBooks()) {
            String bookISBN = book.getISBN();
            for (User user : users) {
                BookCollection userLoanedBooks = FileManager.getUserLoanedBooksData(user);
                for (Book userBook : userLoanedBooks.getBooks()) {
                    if (userBook.getISBN().equalsIgnoreCase(bookISBN)) {
                        user.addBook(allBooks.find(bookISBN).getBooks().get(0));
                        System.out.println("Matched book " + bookISBN + " to user " + user.getUsername());
                    }
                }
            }
        }
    }

    public static ArrayList<User> loadUsersData() {

        ArrayList<User> users = new ArrayList<User>();

        File file = new File(usersPath);

        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\| ");
            User newUser = new User(getValue(parts[0]), getValue(parts[1]), getValue(parts[2]));
            users.add(newUser);
        }

        return users;

    }

    public static BookCollection loadLoanedBooksData() {

        BookCollection allBooks = loadBooksData();
        BookCollection loanedBooks = new BookCollection();

        File file = new File(loanedBooksPath);

        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\| ");
            String bookISBN = getValue(parts[0].trim());
            String userId = getValue(parts[1].trim());

            Book book = allBooks.find(bookISBN).getBooks().get(0);

            // Find user in users
            ArrayList<User> users = loadUsersData();
            for (User u : users) {
                if (u.getID().equalsIgnoreCase(userId)) {
                    u.addBook(book);
                    loanedBooks.addBook(book);
                    break;
                }
            }
        }

        return loanedBooks;
    }

    public static BookCollection getUserLoanedBooksData(User user) {

        BookCollection loanedBooks = loadLoanedBooksData();
        BookCollection userLoanedBooks = new BookCollection();

        for (Book book : loanedBooks.getBooks()) {
            if (book.getUsersLoanedTo().contains(user)) {
                userLoanedBooks.addBook(book);
            }
        }

        return userLoanedBooks;
    }

    public static void updateUsersData(ArrayList<User> users) {

        if (!activated || usersPath == null || usersPath.isEmpty() || users == null || users.isEmpty()) {
            System.out.println("FileManager is not activated or usersPath is empty.");
            return;
        }

        String fileContent = "";

        for (User user : users) {
            fileContent += "Username: " + user.getUsername() + " | Password: " + user.getPassword() + " | ID: " + user.getID() + "\n";
        }

        writeToFile(usersPath, fileContent);
    }

    public static void writeToFile(String filePath, String content) {

        if (!activated || usersPath == null || usersPath.isEmpty() || content == null || content.isEmpty()) {
            System.out.println("FileManager is not activated or usersPath is empty.");
            return;
        }

        File file = new File(filePath);
        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fileContent = "";

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            fileContent += line + "\n";
        }

        try {

            // Write content to file.
            PrintWriter writer = new PrintWriter(filePath);
            writer.write(fileContent + content);
            writer.close();

        } catch (IOException e) {

            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();

        }
    }

    public static void addLoanedBookData(Book book, User user) {
        String dataContent = "ISBN: " + book.getISBN() + " | UserID: " + user.getID() + "\n";
        writeToFile(loanedBooksPath, dataContent);
    }

    public static void removeLoanedBookData(Book book, User user) {
        // Load all loaned books data
        File file = new File(loanedBooksPath);
        ArrayList<String> lines = new ArrayList<String>();

        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            lines.add(line);
        }

        // Rewrite file without the returned book entry
        try {
            PrintWriter writer = new PrintWriter(file);
            for (String line : lines) {
                int amountReturned = 0;
                if(line.contains("ISBN: " + book.getISBN() + " | UserID: " + user.getID()) && amountReturned == 0)
                    amountReturned++;
                else {
                    writer.println(line);
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while updating the loaned books file.");
            e.printStackTrace();
        }
    }
}
