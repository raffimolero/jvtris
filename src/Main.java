import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TetrisGrid tetris = new TetrisGrid();

        // main clock
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tetris.tick();
                tetris.display();
            }
        }, 0, 1000);

        // input reading
        boolean isRunning = true;
        while (isRunning) {
            boolean display = true;
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            char ch = line.isEmpty() ? '\0' : line.charAt(0);
            switch (ch) {
                case 'a' -> tetris.moveBy(-1, 0);
                case 'd' -> tetris.moveBy(1, 0);
                case ' ' -> {}
                case 'q' -> isRunning = false;
                default -> display = false;
            }
            if (display) tetris.display();
        }
    }
}
