public class Palindrome {

    /**
     * Construct a Character Deque where the characters
     * appear in the same order as in the String
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<>();
        int len = word.length();
        for (int i = 0; i < len; i += 1) {
            char ithChar = word.charAt(i); //String class method which gets the ith Char
            wordDeque.addLast(ithChar);
        }
        return wordDeque;
    }


    /**
     * helper function for isPalindrome
     */
    private boolean isMirrorDeque(Deque d) {
        if (d.size() <= 1) {
            return true;
        }
        return (d.removeLast() == d.removeFirst() & isMirrorDeque(d));
    }

    /**
     * Returns true if the given word is a palindrome
     */
    public boolean isPalindrome(String word) {
        Deque theWord = wordToDeque(word);
        return isMirrorDeque(theWord);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        int len = word.length();
        for (int i = 0; i < len / 2; i++) {
            if (!cc.equalChars(word.charAt(i), word.charAt(len - i - 1))) {
                return false;
            }
        }
        return true;
    }
}
