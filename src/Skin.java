import java.awt.*;

public record Skin(Color[] pieces) {
    public static final Skin DEFAULT = new Skin(
        new Color[] {
            Color.GRAY,
            Color.CYAN,
            Color.BLUE,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.PINK,
            Color.RED,
        }
    );

    public Color pieceColor(Piece piece) {
        return pieces[piece.ordinal()];
    }
}
