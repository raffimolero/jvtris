//import javax.swing.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.util.*;
//import java.util.Timer;
//
//public class Main {
//    public static void main(String[] args) {
//        TetrisGrid tetris = new TetrisGrid();
//
//        String[] columns = new String[10];
//        for (int x = 0; x < 10; x++) {
//            columns[x] = "";
//        }
//        final String[][][] grid = {new String[20][10]};
//        for (int y = 0; y < 20; y++) {
//            for (int x = 0; x < 10; x++) {
//                grid[0][y][x] = "" + (x + y) % 7;
//            }
//        }
//
//        Set<Integer> held = new HashSet<>();
//
//        JFrame frame = new JFrame("this is a test");
//        frame.setSize(200, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                System.out.println("pressed " + e.getKeyChar());
//                if (held.add(e.getKeyCode())) {
//                    switch (e.getKeyChar()) {
//                        case 'a' -> tetris.moveBy(-1, 0);
//                        case 'd' -> tetris.moveBy(1, 0);
//                        case ' ' -> {}
//                        default -> {}
//                    }
//                }
//                frame.repaint();
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (held.remove(e.getKeyCode())) {
//                    System.out.println("released " + e.getKeyChar());
//                }
//            }
//        });
//
//
//        JTable table = new JTable(grid[0], columns);
//        frame.add(table);
//
//
//        frame.setVisible(true);
//
//    }
//}
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Main extends JPanel {
    private int HEIGHT = 500;
    private int WIDTH = 400;
    private int size = 15;
    private int margin = 2;
    private static TetrisGrid tetris = new TetrisGrid();

    private JFrame  frame;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileOffset = size + margin;
        int width = tileOffset * tetris.grid.w - margin;
        int height = tileOffset * tetris.grid.h - margin;
        // Offset the position to keep it centered
        int top = (HEIGHT - height) / 2;
        int left = (WIDTH - width) / 2;

        // draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw score
        g.setColor(Color.WHITE);
        g.drawString("" + tetris.score, left, top - 20);

        // draw grid
        for (int y = 0; y < tetris.grid.h; y++) {
            for (int x = 0; x < tetris.grid.w; x++) {
                Piece tile = tetris.grid.getCell(x, y);
                Color col = tetris.skin.pieceColor(tile);
                g.setColor(col);
                g.fillRect(left + x * tileOffset, top + y * tileOffset, size, size);
            }
        }
    }

    public JFrame createAndShowGUI() {
        JFrame frame = new JFrame("Events Demo");
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Add listener to the frame
        frame.addKeyListener(new KeyAdapter() {
            // Key Pressed method
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> tetris.rotateBy(1);
                    case KeyEvent.VK_DOWN -> tetris.tick();
                    case KeyEvent.VK_LEFT -> tetris.moveBy(-1, 0);
                    case KeyEvent.VK_RIGHT -> tetris.moveBy(1, 0);
                }
                repaint();
            }
        });

        return frame;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main project = new Main();
                JFrame frame = project.createAndShowGUI();
                ActionListener tick = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tetris.tick();
                        frame.repaint();
                    }
                };
                Timer timer = new Timer(1000, tick);
                timer.start();
            }
        });
    }
}