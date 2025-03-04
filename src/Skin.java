import java.awt.*;

public record Skin(Color[] pieces) {
    public static final Skin DEFAULT = new Skin(
        new Color[] {
                Color.decode("#202020"),
                Color.CYAN,
                Color.BLUE,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.MAGENTA,
                Color.RED,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.DARK_GRAY,
        }
    );

    public Color pieceColor(Piece piece) {
        return pieces[piece.ordinal()];
    }
}
