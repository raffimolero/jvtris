import java.util.ArrayList;
import java.util.Collections;

public class Grid {
    int w;
    int h;
    ArrayList<Piece> cells;

    public Grid(Grid other) {
        this(other.w, other.h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                setCell(x, y, other.getCell(x, y));
            }
        }
    }

    public Grid(int width, int height) {
        w = width;
        h = height;
        int size = w * h;
        cells = new ArrayList<>(Collections.nCopies(size, Piece.EMPTY));
    }

//    public void display(Skin skin) {
//        // top left corner
//        System.out.print("+-");
//        // top bar
//        for (int x = 0; x < w; x++) {
//            System.out.print("--");
//        }
//        // top right corner
//        System.out.println("+");
//        for (int y = 0; y < h; y++) {
//            // left edge
//            System.out.print("| ");
//            for (int x = 0; x < w; x++) {
//                Piece cell = getCell(x, y);
//                // cell
//                System.out.print(skin.pieceChar(cell) + " ");
//            }
//            // right edge
//            System.out.println("|");
//        }
//        // bottom left corner
//        System.out.print("+-");
//        // bottom bar
//        for (int x = 0; x < w; x++) {
//            System.out.print("--");
//        }
//        // bottom right corner
//        System.out.println("+");
//    }

    int getIndex(int x, int y) {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            return -1;
        }
        return y * w + x;
    }

    public Piece getCell(int x, int y) {
        int i = getIndex(x, y);
        if (i == -1) {
            return Piece.GARBAGE;
        }
        return cells.get(i);
    }

    public void setCell(int x, int y, Piece value) {
        int i = getIndex(x, y);
        if (i == -1) {
            return;
        }
        cells.set(i, value);
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
