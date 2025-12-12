import java.util.ArrayList;

public class Book {

    String title;
    String author;
    String ISBN;
    int pages;
    String language;
    int amountAvailable;
    int totalAmount;
    ArrayList<User> usersLoanedTo;

    public Book(String title, String author, String ISBN, int pages, String language, int totalAmount) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.pages = pages;
        this.language = language;
        this.totalAmount = totalAmount;
        this.amountAvailable = totalAmount;
        this.usersLoanedTo = new ArrayList<User>();
    }

    public boolean isAvailable() {
        return amountAvailable > 0;
    }

    public void checkOut() {
        if (isAvailable()) {
            amountAvailable--;
        } else {
            throw new IllegalStateException("No copies available for checkout.");
        }
    }

    public void returnBook(User user) {
        if (this.usersLoanedTo.contains(user)) {
            this.usersLoanedTo.remove(user);
            amountAvailable++;
        } else {
            throw new IllegalArgumentException("Book is not loaned to selected user.");
        }
    }

}
