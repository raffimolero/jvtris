public class MovableGrid {
    Grid grid;
    int targetX;
    int targetY;

    MovableGrid(MovableGrid piece) {
        grid = new Grid(piece.grid);
        targetX = piece.targetX;
        targetY = piece.targetY;
    }

    MovableGrid(Grid shape, int x, int y) {
        grid = shape;
        targetX = x;
        targetY = y;
    }

    void rotateCw() {
        Grid newData = new Grid(grid.h, grid.w);
        for (int y = 0; y < newData.h; y++) {
            for (int x = 0; x < newData.w; x++) {
                //noinspection SuspiciousNameCombination
                newData.setCell(x, y, grid.getCell(y, grid.w-1 - x));
            }
        }
        grid = newData;
    }

    public void rotate(int timesCw90) {
        for (int i = 0; i < timesCw90; i++) {
            rotateCw();
        }
    }

    public boolean isBlocked(Grid target) {
        for (int y = 0; y < grid.w; y++) {
            for (int x = 0; x < grid.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                if (grid.getCell(x, y).isBlocked() && target.getCell(destX, destY).isBlocked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void unplace(Grid target) {
        for (int y = 0; y < grid.w; y++) {
            for (int x = 0; x < grid.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                Piece cell = grid.getCell(x, y);
                if (cell.isBlocked()) {
                    target.setCell(destX, destY, Piece.EMPTY);
                }
            }
        }
    }

    /**
     * Attempts to place this movable grid inside the parent grid.
     * Throws an exception if something is in the way.
     * @param target the parent grid to place this movable grid on
     */
    public void place(Grid target) {
        assert !isBlocked(target);
        for (int y = 0; y < grid.w; y++) {
            for (int x = 0; x < grid.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                Piece cell = grid.getCell(x, y);
                if (cell.isBlocked()) {
                    target.setCell(destX, destY, cell);
                }
            }
        }
    }
}
