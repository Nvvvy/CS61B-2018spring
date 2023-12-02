import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        /* Return true */
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("a"));

        /* Return false*/
        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("rancor"));
        assertFalse(palindrome.isPalindrome("aaaaaA"));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        CharacterComparator cc = new OffByOne();

        assertFalse(palindrome.isPalindrome("cat", cc));
        assertFalse(palindrome.isPalindrome("%%", cc));

        assertTrue(palindrome.isPalindrome("&%", cc));
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("x", cc));
        assertTrue(palindrome.isPalindrome("", cc));
    }

    @Test
    public void testIsPalindromeOffByN() {
        CharacterComparator cc = new OffByN(5);

        assertTrue(palindrome.isPalindrome("af", cc));
        assertTrue(palindrome.isPalindrome("fa", cc));
        assertTrue(palindrome.isPalindrome("fha", cc));

        assertFalse(palindrome.isPalindrome("fah", cc));
        assertFalse(palindrome.isPalindrome("&%", cc));
    }
}
