import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 20);
        Skin skin = Skin.DEFAULT;
        Queue queue = new Queue(5);
        Timer timer = new Timer();
        final MovableGrid[] movingPiece = {new MovableGrid(new Grid(0, 0), 5, 2)};

        // main clock
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Piece piece = queue.nextPiece();

                movingPiece[0].unplace(grid);
                movingPiece[0] = new MovableGrid(piece.toGrid(), 5, 2);
                movingPiece[0].place(grid);

                grid.display(skin);
            }
        }, 0, 1*1000);

        // input reading
        boolean isRunning = true;
        while (isRunning) {
            boolean display = true;
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            char ch = line.isEmpty() ? '\0' : line.charAt(0);
            switch (ch) {
                case 'a' -> {
                    movingPiece[0].unplace(grid);
                    movingPiece[0].targetX--;
                    if (movingPiece[0].isBlocked(grid)) {
                        movingPiece[0].targetX++;
                    }
                    movingPiece[0].place(grid);
                }
                case 'd' -> {
                    movingPiece[0].unplace(grid);
                    movingPiece[0].targetX++;
                    if (movingPiece[0].isBlocked(grid)) {
                        movingPiece[0].targetX--;
                    }
                    movingPiece[0].place(grid);
                }
                case ' ' -> {
                }
                case 'q' -> {
                    isRunning = false;
                }
                default -> {
                    display = false;
                }
            }
            if (display) {
                grid.display(skin);
            }
        }
    }
}
