public class User {

    String username;
    String password;
    BookCollection loanedBooks;
    String id;

    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.loanedBooks = new BookCollection();
    }

    public String getID() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addBook(Book book) {
        this.loanedBooks.addBook(book);
    }

    public void removeBook(Book book) {
        this.loanedBooks.removeBook(book);
    }

}
