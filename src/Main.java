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

/**
 * TODO:
 * - hard drop
 * - ghost piece
 * - queue
 * - tetris clear effect
 * - score system
 */

public class Main extends JPanel {
    private int HEIGHT = 900;
    private int WIDTH = 600;
    private int size = 25;
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
        int left = (WIDTH - width) / 2;
        int top = (HEIGHT - height) / 2;

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
                g.fillRect(
                        left + x * tileOffset,
                        top + y * tileOffset,
                        size,
                        size);
            }
        }

        // draw held piece
        if (tetris.heldPiece != null) {
            Grid piece = tetris.heldPiece.toGrid();
            double offset = (4 + piece.w) / -2.0;
            double offX = offset - 0.5;
            double offY = offset + 4;
            for (int y = 0; y < piece.h; y++) {
                for (int x = 0; x < piece.w; x++) {
                    Piece tile = piece.getCell(x, y);
                    Color col = tetris.skin.pieceColor(tile);
                    g.setColor(col);
                    g.fillRect(
                            (int)(left + (x + offX) * tileOffset),
                            (int)(top + (y + offY) * tileOffset),
                            size,
                            size);
                }
            }
        }

        // draw queue
        for (int i = 0; i < tetris.queue.targetLen; i++) {
            Grid piece = tetris.queue.queue.get(i).toGrid();
            double offset = (4 + piece.w) / -2.0;
            double offX = offset + 14.5;
            double offY = offset + 4 + i * 4;
            for (int y = 0; y < piece.h; y++) {
                for (int x = 0; x < piece.w; x++) {
                    Piece tile = piece.getCell(x, y);
                    Color col = tetris.skin.pieceColor(tile);
                    g.setColor(col);
                    g.fillRect(
                            (int)(left + (x + offX) * tileOffset),
                            (int)(top + (y + offY) * tileOffset),
                            size,
                            size);
                }
            }
        }

        // draw ghost
        g.setColor(tetris.skin.pieceColor(Piece.GARBAGE));
        if (tetris.currentPiece != null) {
            MovableGrid ghost = tetris.dropGhost();
            for (int y = 0; y < ghost.grid.h; y++) {
                for (int x = 0; x < ghost.grid.w; x++) {
                    int gridX = x + ghost.targetX;
                    int gridY = y + ghost.targetY;
                    Piece tile = ghost.grid.getCell(x, y);
                    if (tile.isEmpty() || tetris.grid.getCell(gridX, gridY).isBlocked()) {
                        continue;
                    }
                    g.fillRect(
                            left + (x + ghost.targetX) * tileOffset,
                            top + (y + ghost.targetY) * tileOffset,
                            size,
                            size);
                }
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
                    case KeyEvent.VK_A -> tetris.hold();
                    case KeyEvent.VK_S -> tetris.rotateBy(3);
                    case KeyEvent.VK_D -> tetris.rotateBy(2);
                    case KeyEvent.VK_F -> tetris.rotateBy(1);
                    case KeyEvent.VK_SPACE -> tetris.hardDrop();
                    case KeyEvent.VK_K -> tetris.softDrop();
                    case KeyEvent.VK_J -> tetris.moveBy(-1, 0);
                    case KeyEvent.VK_L -> tetris.moveBy(1, 0);
                }
                repaint();
            }
        });

        return frame;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                double gravity = 1;
                int tickMs = (int)(1000 / gravity);

                Main project = new Main();
                JFrame frame = project.createAndShowGUI();
                ActionListener tick = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tetris.tick();
                        frame.repaint();
                    }
                };
                Timer timer = new Timer(tickMs, tick);
                timer.start();
            }
        });
    }
}