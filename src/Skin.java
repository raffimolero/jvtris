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
                Color.LIGHT_GRAY,
                Color.DARK_GRAY,
        }
    );

    public Color mixColors(Color primary, Color secondary, double primaryRatio) {
        double secondaryRatio = 1.0 - primaryRatio;
        double red = primary.getRed() * primaryRatio + secondary.getRed() * secondaryRatio;
        double green = primary.getGreen() * primaryRatio + secondary.getGreen() * secondaryRatio;
        double blue = primary.getBlue() * primaryRatio + secondary.getBlue() * secondaryRatio;
        return new Color((int) red, (int) green, (int) blue);
    }

    public Color pieceColor(Piece piece) {
        int ord = piece.ordinal();
        Color primary = pieces[ord];
        if (ord >= Piece.I.ordinal() && ord <= Piece.Z.ordinal()) {
            Color secondary = Color.WHITE;
            return mixColors(primary, secondary, 0.5);
        } else {
            return primary;
        }
    }
}
