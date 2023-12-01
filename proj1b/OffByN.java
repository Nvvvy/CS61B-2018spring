public class OffByN implements CharacterComparator {
    private int absDiff;

    public OffByN(int N) {
        absDiff = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == absDiff;
    }
}
