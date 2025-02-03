public class MovableGrid {
    Grid data;
    int targetX;
    int targetY;

    MovableGrid(Grid shape, int x, int y) {
        data = shape;
        targetX = x;
        targetY = y;
    }

    public void unplace(Grid target) {
        for (int y = 0; y < data.w; y++) {
            for (int x = 0; x < data.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                Piece cell = data.getCell(x, y);
                if (cell.isBlocked()) {
                    target.setCell(destX, destY, Piece.EMPTY);
                }
            }
        }
    }

    public boolean isBlocked(Grid target) {
        for (int y = 0; y < data.w; y++) {
            for (int x = 0; x < data.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                if (data.getCell(x, y).isBlocked() && target.getCell(destX, destY).isBlocked()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Attempts to place this movable grid inside the parent grid.
     * Throws an exception if something is in the way.
     * @param target the parent grid to place this movable grid on
     */
    public void place(Grid target) {
        assert !isBlocked(target);
        for (int y = 0; y < data.w; y++) {
            for (int x = 0; x < data.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                Piece cell = data.getCell(x, y);
                if (cell.isBlocked()) {
                    target.setCell(destX, destY, cell);
                }
            }
        }
    }
}
