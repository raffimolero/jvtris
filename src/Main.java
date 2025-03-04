import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

/**
 * TODO:
 * - better soft drop grace period (sometimes it locks you instantly on softdrop)
 * - tetris clear effect
 * - score system
 */

public class Main extends JPanel {
    private int HEIGHT = 900;
    private int WIDTH = 600;
    private int size = 25;
    private int margin = 2;
    private static GameSettings settings = new GameSettings();
    private static TetrisGrid tetris = new TetrisGrid(settings);
    private static TetrisController input = new TetrisController(tetris);

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
                Color col = settings.skin.pieceColor(tile);
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
                    Color col = settings.skin.pieceColor(tile);
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
                    Color col = settings.skin.pieceColor(tile);
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
        g.setColor(settings.skin.pieceColor(Piece.GHOST));
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
                input.down(e.getKeyCode());
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                input.up(e.getKeyCode());
            }
        });

        return frame;
    }

    public static void main(String[] args) {
        try {
            KickTable.dump();
        } catch (IOException e) {
            System.out.println("could not dump kick table");
            System.out.println(e);
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main project = new Main();
                JFrame frame = project.createAndShowGUI();
                ActionListener tick = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        input.tick();
                        frame.repaint();
                    }
                };
                Timer timer = new Timer(settings.tickMs, tick);
                timer.start();
            }
        });
    }
}