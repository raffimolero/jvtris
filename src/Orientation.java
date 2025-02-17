public enum Orientation {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public Orientation rotate(Orientation other) {
        int ord = this.ordinal() + other.ordinal();
        return Orientation.values()[ord % 4];
    }
}
