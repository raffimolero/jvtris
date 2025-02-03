public class TetrisGrid {
    Grid grid;
    Skin skin;
    Queue queue;
    MovableGrid movingPiece;

    public TetrisGrid() {
        this(Skin.DEFAULT, 5, 10, 20);
    }

    public TetrisGrid(Skin skin, int queueLength, int width, int height) {
        grid = new Grid(width, height);
        this.skin = Skin.DEFAULT;
        queue = new Queue(queueLength);
        movingPiece = new MovableGrid(new Grid(0, 0), width / 2, 2);
    }

    public void lockPiece() {
        Piece piece = queue.nextPiece();
        movingPiece = new MovableGrid(piece.toGrid(), 5, 2);
        movingPiece.place(grid);
    }

    public void tick() {
        if (!moveBy(0, 1)) {
            lockPiece();
        }
    }

//    public void display() {
//        System.out.println("\033[2J"); // clear screen
//        grid.display(skin);
//    }

    public String[][] table() {
        String[][] out = new String[grid.h][grid.w];
        for (int y = 0; y < grid.h; y++) {
            for (int x = 0; x < grid.w; x++) {
                out[y][x] = "" + grid.getCell(x, y);
            }
        }
        return out;
    }

    /**
     * Attempts to move the moving piece by x, y.
     * @param x horizontal displacement, positive to the right
     * @param y vertical displacement, positive to the bottom
     * @return true if the space is not blocked in the target grid; false otherwise.
     */
    public boolean moveBy(int x, int y) {
        // empty tile cannot be moved
        if (movingPiece.data.w * movingPiece.data.h == 0) {
            return false;
        }
        movingPiece.unplace(grid);
        movingPiece.targetX += x;
        movingPiece.targetY += y;
        boolean isBlocked = movingPiece.isBlocked(grid);
        if (isBlocked) {
            movingPiece.targetX -= x;
            movingPiece.targetY -= y;
        }
        movingPiece.place(grid);
        return !isBlocked;
    }
}
