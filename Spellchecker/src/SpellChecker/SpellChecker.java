package SpellChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Trie Node class
class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;

    public TrieNode() {
        children = new TrieNode[26]; // Assuming only lowercase a-z
        isEndOfWord = false;
    }
}

// Trie class
class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        if (word.isEmpty()) return; // Skip empty words
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            // Check if the character is a lowercase letter
            if (c < 'a' || c > 'z') {
                System.out.println("Ignoring invalid character: " + c); // Debugging statement
                return; // Skip invalid characters
            }
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (c < 'a' || c > 'z') return false; // Invalid character check
            int index = c - 'a';
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return node.isEndOfWord;
    }
}

// Main Spell Checker Application
public class SpellChecker {
    private Trie trie;

    public SpellChecker() {
        trie = new Trie();
        loadDictionary("dictionary.txt"); // Load words from the dictionary file
        createAndShowGUI();
    }

    private void loadDictionary(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                String trimmedWord = word.trim().toLowerCase();
                trie.insert(trimmedWord); // Insert each word into the Trie
                System.out.println("Inserted: " + trimmedWord); // Debugging statement
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading dictionary: " + e.getMessage());
            System.exit(1); // Exit the application if the dictionary cannot be loaded
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Spell Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter a word:");
        JTextField textField = new JTextField(20);
        JButton checkButton = new JButton("Check");
        JLabel resultLabel = new JLabel("");

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Corrected line
                String word = textField.getText().trim().toLowerCase(); // Convert to lowercase
                System.out.println("Searching for: " + word); // Debugging statement
                if (trie.search(word)) {
                    resultLabel.setText("Correct: " + word);
                } else {
                    resultLabel.setText("Incorrect: " + word);
                }
                textField.setText(""); // Clear the text field after checking
            }
        });

        frame.add(label);
        frame.add(textField);
        frame.add(checkButton);
        frame.add(resultLabel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SpellChecker::new);
    }
}