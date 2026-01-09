import java.util.ArrayList;

public class Book {

    String title;
    String author;
    String ISBN;
    int pages;
    String language;
    int year;
    int amountAvailable;
    int totalAmount;
    ArrayList<User> usersLoanedTo;

    public Book(String title, String author, int pages, String language, int year, String ISBN, int totalAmount) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.pages = pages;
        this.language = language;
        this.year = year;
        this.totalAmount = totalAmount;
        this.amountAvailable = totalAmount;
        this.usersLoanedTo = new ArrayList<User>();
    }

    public boolean isAvailable() {
        return amountAvailable > 0;
    }

    public void checkOut(User user) {
        if (isAvailable()) {
            user.addBook(this);
            this.usersLoanedTo.add(user);
            amountAvailable--;
        } else {
            throw new IllegalStateException("No copies available for checkout.");
        }
    }

    public void returnBook(User user) {
        if (this.usersLoanedTo.contains(user)) {
            user.removeBook(this);
            this.usersLoanedTo.remove(user);
            amountAvailable++;
        } else {
            throw new IllegalStateException("Book is not loaned to selected user.");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getPages() {
        return pages;
    }

    public String getLanguage() {
        return language;
    }

    public int getYear() {
        return year;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public ArrayList<User> getUsersLoanedTo() {
        return usersLoanedTo;
    }

}
