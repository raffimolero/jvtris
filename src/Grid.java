import java.util.ArrayList;
import java.util.Collections;

public class Grid {
    int w;
    int h;
    ArrayList<Piece> cells;

    public Grid(int width, int height) {
        w = width;
        h = height;
        int size = w * h;
        cells = new ArrayList<Piece>(Collections.nCopies(size, Piece.E));
    }

    public void display(Skin skin) {
        for (int y = 0; y < h; y++) {
            System.out.print("| ");
            for (int x = 0; x < w; x++) {
                Piece cell = getCell(x, y);
                System.out.print(skin.pieceChar(cell) + " ");
            }
            System.out.println("|");
        }
    }

    int getIndex(int x, int y) {
        return y * w + x;
    }

    public Piece getCell(int x, int y) {
        int i = getIndex(x, y);
        return cells.get(i);
    }

    public void setCell(int x, int y, Piece value) {
        int i = getIndex(x, y);
        cells.set(i, value);
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
