import java.util.*;

public class AhoCorasick implements SearchMotor{

    private Node root = new Node();
    Map<Integer, Integer> keywordHits = new HashMap<>();

    @Override
    public BookCollection search(String text) {
        return null;
    }

    private class Keyword {
        int id;
        String word;
        int weight; // importance
    }


    // Inner Node class
    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node fail;
        List<Keyword> output = new ArrayList<>();
    }
    void insert_data(int id, String word, int weight){
        Keyword key = new Keyword();
        key.id = id;
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
    void score(String text, Map<Integer, Integer> keywordHits) {
        Node current = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            while (current != root && !current.children.containsKey(c)) {
                current = current.fail;
            }

            current = current.children.getOrDefault(c, root);

            for (Keyword k : current.output) {
                keywordHits.merge(k.id, k.weight, Integer::sum);
            }
        }
    }

}

