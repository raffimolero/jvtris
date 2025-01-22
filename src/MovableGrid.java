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
                if (!cell.isEmpty()) {
                    target.setCell(destX, destY, Piece.E);
                }
            }
        }
    }

    public boolean isBlocked(Grid target) {
        for (int y = 0; y < data.w; y++) {
            for (int x = 0; x < data.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                if (!target.getCell(destX, destY).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Attempts to place this movable grid inside the parent grid.
     * Does nothing if something is in the way.
     * @param target the parent grid to place this movable grid on
     * @return true if the placement was successful; false if something was blocking placement.
     */
    public boolean place(Grid target) {
        if (isBlocked(target)) {
            return false;
        }
        for (int y = 0; y < data.w; y++) {
            for (int x = 0; x < data.h; x++) {
                int destX = targetX + x;
                int destY = targetY + y;
                Piece cell = data.getCell(x, y);
                target.setCell(destX, destY, cell);
            }
        }
        return true;
    }
}
