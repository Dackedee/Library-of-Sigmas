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

    public boolean contains(Book book) {
        return this.books.contains(book);
    }

    public BookCollection find(String search) {
        BookCollection results = new BookCollection();

        for (Book book : this.books) {
            if (book.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(search.toLowerCase()) ||
                book.getISBN().toLowerCase().contains(search.toLowerCase())) {
                results.addBook(book);
            }
        }

        return results;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

}
