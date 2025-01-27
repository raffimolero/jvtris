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
//        Timer timer = new Timer();
//
//        // main clock
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                tetris.tick();
//                grid[0] = tetris.table();
//            }
//        }, 0, 1000);
//    }
//}
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Main extends JPanel {
    private int HEIGHT = 500;
    private int WIDTH = 400;
    private int size = 50;

    private JFrame  frame;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        // Offset the position to keep it centered
        int pos = 150 - (int) (size * 0.5);

        // Draw the rectangle
        g.fillRect(pos, pos, size, size);
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame("Events Demo");
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        // Add listener to the frame
        frame.addKeyListener(new KeyAdapter() {
            // Key Pressed method
            public void keyPressed(KeyEvent e) {
                // Check if an up key was pressed
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    size += 5;
                }
                // Check if a down key was pressed
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    size -=5;
                }
                // Call the repaint
                repaint();
            }
        });
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main project = new Main();
                project.createAndShowGUI();
            }
        });
    }
}