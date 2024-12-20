/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        // get the greatest length of all strings
        int len = 0;
        for (String s : asciis) {
            len = Math.max(s.length(), len);
        }

        // fill the String to length 'len'
        String[] sorted = new String[asciis.length];
        System.arraycopy(asciis, 0, sorted, 0, asciis.length);
        for (int i = 1; i <= len; i++) {
            sortHelperLSD(sorted, len - i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // build count list
        int[] count = new int[256];
        for (String s : asciis) {
            if (index >= s.length()) {
                count[0] += 1;
            } else {
                int code = (int) s.charAt(index);
                count[code] += 1;
            }
        }

        // build start list
        int[] start = new int[256];
        int pos = 0;
        for (int i = 0; i < 256; i++) {
            start[i] = pos;
            pos += count[i];
        }

        // sort according to the index digit
        String[] sorted = new String[asciis.length];
        for (String s : asciis) {
            int code = index >= s.length() ? 0 : (int) s.charAt(index);
            int place = start[code];
            sorted[place] = s;
            start[code] += 1;
        }
        System.arraycopy(sorted, 0, asciis, 0, asciis.length);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
