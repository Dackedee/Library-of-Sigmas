public class User {

    String username;
    String password;
    BookCollection loanedBooks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loanedBooks = new BookCollection();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
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
