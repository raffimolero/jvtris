import java.util.Random;

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

    public void load(Preset p) {
        String[] lines = p.pattern().split("\\n");
        for (int y = 0; y < grid.h; y++) {
            String line = lines[y].trim();
            for (int x = 0; x < grid.w; x++) {
                grid.setCell(x, y, Piece.fromChar(line.charAt(x)));
            }
        }

        queue.queue.clear();
        for (int i = 0; i < p.queue().length(); i++) {
            queue.queue.add(Piece.fromChar(p.queue().charAt(i)));
        }
        queue.refill();
    }

    private void set(int queueLength, int width, int height) {
        alive = true;
        held = false;
        grid = new Grid(width, height);
        queue = new PieceQueue(queueLength);
        movingPiece = new MovableGrid(Piece.EMPTY, width / 2, 2);
        currentPiece = null;
        heldPiece = null;
        score = 0;
    }

    private void nextPiece() {
        Grid piece = currentPiece.toGrid();
        movingPiece = new MovableGrid(currentPiece, 5 - ((piece.w + 1) / 2), 0);
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
            refillGarbage();
        }
    }

    public void refillGarbage() {
        if (settings.garbageHeight <= 0) {
            return;
        }
        int targetGarbageRow = grid.h - settings.garbageHeight;
        while (!rowHasGarbage(targetGarbageRow)) {
            addGarbage();
        }
    }

    private boolean rowHasGarbage(int row) {
        boolean cell0IsGarbage = grid.getCell(0, row) == Piece.GARBAGE;
        boolean cell1IsGarbage = grid.getCell(1, row) == Piece.GARBAGE;
        return cell0IsGarbage || cell1IsGarbage;
    }

    /// Adds 1 line of garbage with a hole at a random x position different from the previous hole position
    private int previousHoleX = -1;
    public void addGarbage() {
        int x;
        if (previousHoleX == -1) {
            x = new Random().nextInt(10);
        } else {
            x = new Random().nextInt(9);
            if (x == previousHoleX) {
                x = 9;
            }
        }
        addGarbage(x);
        previousHoleX = x;
    }

    /// Adds 1 line of garbage with a hole at the specified x position
    public void addGarbage(int holeX) {
        // remove player piece
        movingPiece.unplace(grid);

        // shift tiles up
        for (int y = 0; y < grid.h - 1; y++) {
            for (int x = 0; x < grid.w; x++) {
                grid.setCell(x, y, grid.getCell(x, y + 1));
            }
        }

        // add garbage line
        int bottom = grid.h - 1;
        for (int x = 0; x < grid.w; x++) {
            grid.setCell(x, bottom, Piece.GARBAGE);
        }
        // add hole
        grid.setCell(holeX, bottom, Piece.EMPTY);

        // put player piece back
        if (movingPiece.isBlocked(grid)) {
            alive = false;
        } else {
            movingPiece.place(grid);
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
                    case ROTATE_CW -> { return rotateBy(Orientation.RIGHT); }
                    case ROTATE_180 -> { return rotateBy(Orientation.DOWN); }
                    case ROTATE_CC -> { return rotateBy(Orientation.LEFT); }
                    case LEFT -> { return moveBy(-1, 0); }
                    case RIGHT -> { return moveBy(1, 0); }
                    case HOLD -> { return hold(); }
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
                    case SOFT_DROP ->  { return instantMove(0, 1); }
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
     * @param delta the number of times to rotate the piece clockwise.
     * @return true if the space is not blocked in the target grid; false otherwise.
     */
    public boolean rotateBy(Orientation delta) {
        if (!alive) {
            return false;
        }
        // empty tile cannot be rotated
        if (movingPiece.grid.w * movingPiece.grid.h == 0) {
            return false;
        }

        // get kick table
        Point[] table = settings.kickTable.kicks(movingPiece.piece, movingPiece.orientation, delta);

        movingPiece.unplace(grid);
        movingPiece.rotate(delta);

        // check kick table
        boolean isBlocked = false;
        System.out.println("TESTING");
        for (Point p : table) {
            movingPiece.targetX += p.x();
            movingPiece.targetY -= p.y();

            isBlocked = movingPiece.isBlocked(grid);
            if (!isBlocked) {
                break;
            }
            movingPiece.targetX -= p.x();
            movingPiece.targetY += p.y();
        }

        if (isBlocked) {
            movingPiece.rotate(Orientation.values()[4 - delta.ordinal()]);
        }
        movingPiece.place(grid);
        return !isBlocked;
    }
}
