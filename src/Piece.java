public enum Piece {
    EMPTY,
    I,
    J,
    L,
    O,
    S,
    T,
    Z,
    GARBAGE,
    GHOST;

    public static final Piece[] PIECES = { I, J, L, O, S, T, Z };

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isBlocked() {
        return this != EMPTY;
    }

    public Grid toGrid() {
        Grid out;

        // it works ok
        switch (this) {
            case I -> {
                out = new Grid(4, 4);
                out.setCell(0, 1, this);
                out.setCell(1, 1, this);
                out.setCell(2, 1, this);
                out.setCell(3, 1, this);
            }
            case J -> {
                out = new Grid(3, 3);
                out.setCell(0, 0, this);
                out.setCell(0, 1, this);
                out.setCell(1, 1, this);
                out.setCell(2, 1, this);
            }
            case L -> {
                out = new Grid(3, 3);
                out.setCell(2, 0, this);
                out.setCell(0, 1, this);
                out.setCell(1, 1, this);
                out.setCell(2, 1, this);
            }
            case O -> {
                out = new Grid(2, 2);
                out.setCell(0, 0, this);
                out.setCell(0, 1, this);
                out.setCell(1, 0, this);
                out.setCell(1, 1, this);
            }
            case S -> {
                out = new Grid(3, 3);
                out.setCell(1, 0, this);
                out.setCell(2, 0, this);
                out.setCell(0, 1, this);
                out.setCell(1, 1, this);
            }
            case T -> {
                out = new Grid(3, 3);
                out.setCell(1, 0, this);
                out.setCell(0, 1, this);
                out.setCell(1, 1, this);
                out.setCell(2, 1, this);
            }
            case Z -> {
                out = new Grid(3, 3);
                out.setCell(0, 0, this);
                out.setCell(1, 0, this);
                out.setCell(1, 1, this);
                out.setCell(2, 1, this);
            }
            default -> {
                out = new Grid(0, 0);
            }
        }
        return out;
    }
}
