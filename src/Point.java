public record Point(int x, int y) {
    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    public Point neg() {
        return new Point(-x, -y);
    }
}
