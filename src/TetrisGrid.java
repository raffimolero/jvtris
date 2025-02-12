public class TetrisGrid {
    public GameSettings settings;
    public boolean alive;
    public boolean held;
    public int score;
    Grid grid;
    PieceQueue queue;
    Piece heldPiece;
    Piece currentPiece;
    MovableGrid movingPiece;

    public TetrisGrid(GameSettings settings) {
        this(settings, 5, 10, 20);
    }

    public TetrisGrid(GameSettings settings, int queueLength, int width, int height) {
        this.settings = settings;
        set(queueLength, width, height);
    }

    public void reset() {
        set(queue.targetLen, grid.w, grid.h);
    }

    private void set(int queueLength, int width, int height) {
        alive = true;
        held = false;
        grid = new Grid(width, height);
        queue = new PieceQueue(queueLength);
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
        held = false;
    }

    public boolean hold() {
        if (!alive) {
            return false;
        }
        if (held) {
            return false;
        }
        movingPiece.unplace(grid);
        Piece tmp = currentPiece;
        currentPiece = heldPiece;
        heldPiece = tmp;
        if (currentPiece == null) {
            currentPiece = queue.nextPiece();
        }
        nextPiece();
        held = true;
        return true;
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
        if (movingPiece.isBlocked(grid)) {
            alive = false;
        } else {
            movingPiece.place(grid);
        }
        return linesCleared;
    }

    public boolean onGround() {
        if (moveBy(0, 1)) {
            moveBy(0, -1);
            return false;
        }
        return true;
    }

    public void gravity() {
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
        gravity();
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

    /**
     * reacts to a GameEvent as input
     * @param e the game event to react to
     * @return whether the input was successful
     */
    public boolean input(GameEvent e) {
        switch (e.source()) {
            case INPUT -> {
                switch (e.kind()) {
                    case ROTATE_CW -> { return rotateBy(1); }
                    case ROTATE_180 -> { return rotateBy(2); }
                    case ROTATE_CC -> { return rotateBy(3); }
                    case LEFT -> { return moveBy(-1, 0); }
                    case RIGHT -> { return moveBy(1, 0); }
                    case HOLD -> { return hold(); }
                    case SOFT_DROP ->  { return instantMove(0, 1); }
                    case HARD_DROP -> hardDrop();
                    case RESET -> reset();
                    default -> {}
                }
            }
            case WORLD -> {
                switch (e.kind()) {
                    case TICK -> gravity();
                    case LEFT -> { return instantMove(-1, 0); }
                    case RIGHT -> { return instantMove(1, 0); }
                    default -> {}
                }
            }
        }
        return false;
    }

    public boolean instantMove(int dx, int dy) {
        boolean out = false;
        while (moveBy(dx, dy)) {
            out = true;
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
