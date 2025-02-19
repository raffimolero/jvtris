public class KickTable {
    private static final Point[] noOff = { new Point(0, 0) };
    private static final Point[] noOffx5 = {
            new Point(0, 0),
            new Point(0, 0),
            new Point(0, 0),
            new Point(0, 0),
            new Point(0, 0),
    };

    private static final Point[] off3x3R = {
            new Point(0, 0),
            new Point(1, 0),
            new Point(1, -1),
            new Point(0, 2),
            new Point(1, 2),
    };
    private static final Point[][] off3x3R90 = {
            noOffx5,
            off3x3R,
            noOffx5,
            mirror(off3x3R),
    };

    private static final Point[][] off4x4R90 = removeBaseOffset(new Point[][]{
            { new Point(0, 0), new Point(-1, 0), new Point(2, 0), new Point(-1, 0), new Point(2, 0) },
            { new Point(-1, 0), new Point(0, 0), new Point(0, 0), new Point(0, 1), new Point(0, -2) },
            { new Point(-1, 1), new Point(1, 1), new Point(-2, 1), new Point(1, 0), new Point(-2, 0) },
            { new Point(0, 1), new Point(0, 1), new Point(0, 1), new Point(0, -1), new Point(0, 2) },
    });

    public static final Point[] offNorthR180 = {
            new Point(0, 0),
            new Point(0, 1),
            new Point(1, 1),
            new Point(-1, 1),
            new Point(1, 0),
            new Point(-1, 0)
    };
    private static final Point[] offEastR180 = {
            new Point(0, 0),
            new Point(1, 0),
            new Point(1, 2),
            new Point(1, 1),
            new Point(0, 2),
            new Point(0, 1),
    };
    private static final Point[][] offR180 = {
            offNorthR180,
            offEastR180,
            neg(offNorthR180),
            mirror(offEastR180),
    };

    private static Point[][] removeBaseOffset(Point[][] input) {
        Point[][] out = new Point[4][5];
        for (int orient = 0; orient < 4; orient++) {
            Point off = input[orient][0].neg();
            for (int i = 0; i < 5; i++) {
                out[orient][i] = input[orient][i].add(off);
            }
        }
        return out;
    }

    public static Point[] mirror(Point[] ps) {
        Point[] out = new Point[ps.length];
        for (int i = 0; i < ps.length; i++) {
            out[i] = new Point(-ps[i].x(), ps[i].y());
        }
        return out;
    }

    public static Point[] neg(Point[] in) {
        Point[] out = new Point[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].neg();
        }
        return out;
    }

    public static Point[] diff(Point[] a, Point[] b) {
        Point[] out = new Point[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i].add(b[i].neg());
        }
        return out;
    }

    private Point[] kicksInternal(Point[][] r90, Point[][] r180, Orientation from, Orientation delta) {
        System.out.println("from = " + from);
        int a = from.ordinal();
        int b = (a + delta.ordinal()) % 4;
        switch (delta) {
            case UP -> { return noOff; }
            case LEFT, RIGHT -> { return diff(r90[a], r90[b]); }
            case DOWN -> { return r180[a]; }
        }

        System.out.println("ERROR: kick did not match anything");
        return noOff;
    }

    public Point[] kicks(Piece kind, Orientation from, Orientation delta) {
        switch (kind) {
            case O -> { return noOff; }
            case I -> { return kicksInternal(off4x4R90, offR180, from, delta); }
            case J, L, S, T, Z -> { return kicksInternal(off3x3R90, offR180, from, delta); }
        }
        System.out.println("Tried to kick an invalid piece");
        return noOff;
    }
}
