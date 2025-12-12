import java.util.ArrayList;

public class BookCollection {

    String name;
    String address;
    ArrayList<Book> books;

    public BookCollection(String name, String address, ArrayList<Book> books) {
        this.name = name;
        this.address = address;
        this.books = books;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Book book) {
        int bookIndex = this.books.indexOf(book);

        if (bookIndex == -1) {
            throw new IllegalArgumentException("Book does not exist in BookCollection.");
        } else {
            this.books.remove(bookIndex);
        }
    }

}
