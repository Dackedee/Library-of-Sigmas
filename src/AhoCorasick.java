import java.util.*;

public class AhoCorasick implements SearchMotor{
    Map<Book, Integer> keywordHits = new HashMap<>();
    BookCollection allBooks;
    AhoCorasick(BookCollection books){
        allBooks = books;
        indexBooks(books.getBooks());
    }

    private Node root = new Node();



    @Override
    public BookCollection search(String text) {

            keywordHits.clear();
            score(text, keywordHits);

            List<Map.Entry<Book, Integer>> ranked =
                    new ArrayList<>(keywordHits.entrySet());

            ranked.sort(Map.Entry.<Book, Integer>comparingByValue().reversed());

            BookCollection result = new BookCollection();

            for (var entry : ranked) {
                result.addBook(entry.getKey());
            }

            return result;

    }
    void indexBooks(List<Book> books) {
        for (Book b : books) {
            insertWordPrefixes(b, b.title, 10);

            insertWordPrefixes(b, b.author, 8);

            insertWordPrefixes(b, b.language, 4);
        }

        buildFailureLinks();
    }
    private class Keyword {
        Book book;
        String word;
        int weight;
    }


    // Inner Node class
    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node fail;
        List<Keyword> output = new ArrayList<>();
    }

    private void insertWordPrefixes(Book book, String word, int weight) {
        for (int i = 1; i <= word.length(); i++) {
            String prefix = word.substring(0, i);
            insertData(book, prefix, weight);
        }
    }
    void insertData(Book book, String word, int weight){
        Keyword key = new Keyword();
        key.book = book;
        key.word = word;
        key.weight = weight;
        this.insert(key);
    }
    // Insert pattern
    void insert(Keyword k) {
        Node current = root;
        for (char c : k.word.toCharArray()) {
            current = current.children.computeIfAbsent(c, x -> new Node());
        }
        current.output.add(k);
    }

    // Build failure links
    public void buildFailureLinks() {
        Queue<Node> queue = new LinkedList<>();

        for (Node child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();

                Node fallback = current.fail;
                while (fallback != null && !fallback.children.containsKey(c)) {
                    fallback = fallback.fail;
                }

                if (fallback == null) {
                    child.fail = root;
                } else {
                    child.fail = fallback.children.get(c);
                    child.output.addAll(child.fail.output);
                }

                queue.add(child);
            }
        }
    }

    // Search text
    void score(String text, Map<Book, Integer> keywordHits) {
        Node current = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            while (current != root && !current.children.containsKey(c)) {
                current = current.fail;
            }

            current = current.children.getOrDefault(c, root);

            for (Keyword k : current.output) {
                keywordHits.merge(k.book, k.weight, Integer::sum);
            }
        }
    }

}

