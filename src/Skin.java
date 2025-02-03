import java.awt.*;

public record Skin(Color[] pieces) {
    public static final Skin DEFAULT = new Skin(
        new Color[] {
            Color.DARK_GRAY,
            Color.CYAN,
            Color.BLUE,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
        }
    );

    public Color pieceColor(Piece piece) {
        return pieces[piece.ordinal()];
    }
}
