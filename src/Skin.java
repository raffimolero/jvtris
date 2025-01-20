public record Skin(char[] pieces) {
    public static final Skin DEFAULT = new Skin(".IJLOSTZ".toCharArray());

    public char pieceChar(Piece piece) {
        return pieces[piece.ordinal()];
    }
}
