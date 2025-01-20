public enum Piece {
    E,
    I,
    J,
    L,
    O,
    S,
    T,
    Z;

    public static final Piece[] PIECES = { I, J, L, O, S, T, Z };

    public boolean isEmpty() {
        return this == E;
    }
}
