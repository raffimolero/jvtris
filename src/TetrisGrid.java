public class TetrisGrid {
    public boolean alive;
    Skin skin;
    Grid grid;
    Queue queue;
    Piece heldPiece;
    Piece currentPiece;
    MovableGrid movingPiece;
    public int score;

    public TetrisGrid() {
        this(Skin.DEFAULT, 5, 10, 20);
    }

    public TetrisGrid(Skin skin, int queueLength, int width, int height) {
        alive = true;
        grid = new Grid(width, height);
        this.skin = Skin.DEFAULT;
        queue = new Queue(queueLength);
        movingPiece = new MovableGrid(new Grid(0, 0), width / 2, 2);
        currentPiece = null;
        heldPiece = null;
        score = 0;
    }

    private void nextPiece() {
        Grid piece = currentPiece.toGrid();
        movingPiece = new MovableGrid(piece, 5 - ((piece.w + 1) / 2), 0);
        if (movingPiece.isBlocked(grid)) {
            alive = false;
        }
        movingPiece.place(grid);
    }

    public void hold() {
        if (!alive) {
            return;
        }
        movingPiece.unplace(grid);
        Piece tmp = currentPiece;
        currentPiece = heldPiece;
        heldPiece = tmp;
        if (currentPiece == null) {
            currentPiece = queue.nextPiece();
        }
        nextPiece();
    }

    public void lockPiece() {
        currentPiece = queue.nextPiece();
        Grid nextPiece = currentPiece.toGrid();
        nextPiece();
    }

    /**
     * checks for any full rows on the board.
     * @return the number of lines cleared
     */
    public int clearLines() {
        movingPiece.unplace(grid);
        int linesCleared = 0;
        for (int y = grid.h - 1; y >= 0; y--) {
            boolean cleared = true;
            for (int x = 0; x < grid.w; x++) {
                if (grid.getCell(x, y).isEmpty()) {
                    cleared = false;
                    break;
                }
            }
            if (cleared) {
                linesCleared++;
                // shift tiles down
                for (int y2 = y; y2 > 0; y2--) {
                    for (int x = 0; x < grid.w; x++) {
                        grid.setCell(x, y2, grid.getCell(x, y2 - 1));
                    }
                }
                y++;
            }
        }
        // TODO: what if movingPiece is blocked
        movingPiece.place(grid);
        return linesCleared;
    }

    public void tick() {
        if (!alive) {
            return;
        }
        softDrop();
    }

    public void softDrop() {
        if (!alive) {
            return;
        }
        if (!moveBy(0, 1)) {
            lockPiece();
            score += clearLines();
        }
    }

    public void hardDrop() {
        if (!alive) {
            return;
        }
        while (moveBy(0, 1)) {}
        softDrop();
    }

    public MovableGrid dropGhost() {
        movingPiece.unplace(grid);
        MovableGrid ghost = new MovableGrid(movingPiece);

        while (!ghost.isBlocked(grid)) {
            ghost.targetY += 1;
        }
        ghost.targetY -= 1;
        movingPiece.place(grid);
        return ghost;
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

    public void input(GameEvent e) {
        switch (e.source()) {
            case INPUT -> {
                switch (e.kind()) {
                    case ROTATE_CW -> rotateBy(1);
                    case ROTATE_180 -> rotateBy(2);
                    case ROTATE_CC -> rotateBy(3);
                    case LEFT -> moveBy(-1, 0);
                    case RIGHT -> moveBy(1, 0);
                    // TODO: limit hold to once per piece
                    case HOLD -> hold();
                    case SOFT_DROP ->  { while (moveBy(0, 1)) {} }
                    case HARD_DROP -> hardDrop();
                    default -> {}
                }
            }
            case WORLD -> {
                switch (e.kind()) {
                    case TICK -> softDrop();
                    case LEFT -> { while (moveBy(-1, 0)) {} }
                    case RIGHT -> { while (moveBy(1, 0)) {} }
                    default -> {}
                }
            }
        }
    }

    /**
     * Attempts to move the moving piece by x, y.
     * @param x horizontal displacement, positive to the right
     * @param y vertical displacement, positive to the bottom
     * @return true if the space is not blocked in the target grid; false otherwise.
     */
    public boolean moveBy(int x, int y) {
        if (!alive) {
            return false;
        }
        // empty tile cannot be moved
        if (movingPiece.grid.w * movingPiece.grid.h == 0) {
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

    /**
     * Attempts to rotate the moving piece 90 degrees clockwise times the argument.
     * rotation happens at once; there are no intermediate steps.
     * @param timesCw90 the number of times to rotate the piece clockwise.
     * @return true if the space is not blocked in the target grid; false otherwise.
     */
    public boolean rotateBy(int timesCw90) {
        if (!alive) {
            return false;
        }
        // empty tile cannot be rotated
        if (movingPiece.grid.w * movingPiece.grid.h == 0) {
            return false;
        }
        movingPiece.unplace(grid);
        timesCw90 %= 4;
        movingPiece.rotate(timesCw90);
        boolean isBlocked = movingPiece.isBlocked(grid);
        if (isBlocked) {
            movingPiece.rotate(4 - timesCw90);
        }
        movingPiece.place(grid);
        return !isBlocked;
    }
}
