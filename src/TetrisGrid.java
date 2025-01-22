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

    public void tick() {
        Piece piece = queue.nextPiece();
        movingPiece.unplace(grid);
        movingPiece = new MovableGrid(piece.toGrid(), 5, 2);
        movingPiece.place(grid);
    }

    public void display() {
        System.out.println("\033[2J"); // clear screen
        grid.display(skin);
    }

    /**
     * Attempts to move the moving piece by x, y.
     * @param x horizontal displacement, positive to the right
     * @param y vertical displacement, positive to the bottom
     * @return true if the space is not blocked in the target grid; false otherwise.
     */
    public boolean moveBy(int x, int y) {
        movingPiece.unplace(grid);
        movingPiece.targetX += x;
        movingPiece.targetY += y;
        boolean isBlocked = movingPiece.isBlocked(grid);
        if (isBlocked) {
            movingPiece.targetX -= x;
            movingPiece.targetY -= y;
        }
        movingPiece.place(grid);
        return isBlocked;
    }
}
