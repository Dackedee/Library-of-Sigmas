import java.util.ArrayList;

public class BookCollection {

    String name;
    String address;
    ArrayList<Book> books;

    public BookCollection(ArrayList<Book> books) {
        this.books = books;
    }

    public BookCollection() {
        this.books = new ArrayList<Book>();
    }

    public void addBook(Book book) {
        if (this.books.contains(book)) {
            throw new IllegalArgumentException("Supplied book is already in BookCollection.");
        } else {
            this.books.add(book);
        }
    }

    public void removeBook(Book book) {
        int bookIndex = this.books.indexOf(book);

        if (bookIndex == -1) {
            throw new IllegalArgumentException("Book does not exist in BookCollection.");
        } else {
            this.books.remove(bookIndex);
        }
    }

    public boolean contains(Book book) {
        return this.books.contains(book);
    }

}
