public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 20);
        Skin skin = Skin.DEFAULT;
        Queue queue = new Queue(5);
//        for (int i = 0; i < 15; i++) {
//            Piece piece = queue.nextPiece();
//            System.out.println("piece = " + piece);
//        }
        Piece piece = queue.nextPiece();
        MovableGrid movingPiece = new MovableGrid(piece.toGrid(), 5, 2);
        movingPiece.place(grid);
        grid.display(skin);
    }
}