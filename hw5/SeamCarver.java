import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private Picture pic;
    private int W;
    private int H;

    public SeamCarver(Picture picture) {
        pic = picture;
        W = pic.width();
        H = pic.height();
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return W;
    }

    // height of current picture
    public int height() {
        return H;
    }

    /** energy of pixel at column x and row y */
    public double energy(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        Color left = (x == 0) ? pic.get(W - 1, y) : pic.get(x - 1, y);
        Color right = (x == W - 1) ? pic.get(0, y) : pic.get(x + 1, y);
        Color up = (y == 0) ? pic.get(x, H - 1) : pic.get(x, y - 1);
        Color down = (y == H - 1) ? pic.get(x, 0) : pic.get(x, y + 1);
        return gradient(left, right) + gradient(up, down);
    }

    // Calculates the linear or vertical gradient with two neighbours' color
    private double gradient(Color a, Color b) {
        return Math.pow(a.getRed() - b.getRed(), 2) +
                Math.pow(a.getGreen() - b.getGreen(), 2) +
                Math.pow(a.getBlue() - b.getBlue(), 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture tansPosed = new Picture(H, W);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                tansPosed.set(H - y - 1, x, pic.get(x, y));
            }
        }

        // swap pic and tansPosed, including H and W instance
        Picture raw = pic;
        pic = tansPosed;
        W = tansPosed.width();
        H = tansPosed.height();

        // find horizontalSeam and transfer the coordinate
        int[] horizontalSeam = findVerticalSeam();
        for (int i = 0; i < H; i++) {
            horizontalSeam[i] = W - horizontalSeam[i] - 1;
        }

        // swap again
        pic = raw;
        W = raw.width();
        H = raw.height();
        return horizontalSeam;
    }

    /**
     * sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        int[] seam = new int[H];
        // find minEnergy
        double[][] totalEng = energyMatrix();
        double min = Arrays.stream(totalEng[H - 1]).min().getAsDouble();
        for (int x = 0; x < W; x++) {
            if (totalEng[H - 1][x] == min) {
                seam[H - 1] = x;
            }
        }
        findSeamSeq(seam, totalEng);
        return seam;
    }

    // find the min energy seam from the energyMatrix in reverse order
    private void findSeamSeq(int[] seam, double[][] totalEng) {
        int x = seam[H - 1];
        for (int y = H - 2; y >= 0; y--) {
            double left = (x == 0) ? Double.MAX_VALUE : totalEng[y][x - 1];
            double mid = totalEng[y][x];
            double right = (x == W - 1) ? Double.MAX_VALUE : totalEng[y][x + 1];
            double minEng = Math.min(mid, Math.min(left, right));
            if (mid == minEng) {
                seam[y] = x;
            } else if (left == minEng) {
                seam[y] = x - 1;
                x -= 1;
            } else {
                seam[y] = x + 1;
                x += 1;
            }
        }
    }

    // A helper method for findVerticalSeam to get the Min energy matrix
    private double[][] energyMatrix() {
        double[][] totalEng = new double[H][W];
        for (int y = 0; y < H; y++) {
            for(int x = 0; x < W; x++) {
                if (y == 0) {
                    totalEng[y][x] = energy(x, 0);
                } else {
                    double topLeft = (x == 0) ? Double.MAX_VALUE : totalEng[y - 1][x - 1];
                    double topRight = (x == W - 1) ? Double.MAX_VALUE : totalEng[y - 1][x + 1];
                    totalEng[y][x] = energy(x, y) +
                            Math.min(totalEng[y - 1][x], Math.min(topLeft, topRight));
                }
            }
        }
        return totalEng;
    }


    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != W || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        H -= 1;
        SeamRemover.removeHorizontalSeam(pic, seam);
    }

    // remove vertical seam from picture
    public  void removeVerticalSeam(int[] seam) {
        if (seam.length != H || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        W -= 1;
        SeamRemover.removeVerticalSeam(pic, seam);
    }

    // Returns false if two consecutive position in seam differ by more than 1
    private boolean isValidSeam(int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                return false;
            }
        }
        return true;
    }
}
