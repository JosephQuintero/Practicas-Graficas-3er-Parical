//Joseph Ivan Quintero Carrasco

package Prt_03_Traslacion_3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TraslacionCuboParalelo_3D extends JPanel{
    
    BufferedImage buffer;
    private int WIDTH;
    private int HEIGHT;
    private int[] coordenada = {-100, -100, -100};
    private int zPlano = 2;
    private int size = 50;

    public TraslacionCuboParalelo_3D(Color color, int width, int height) {
        setBackground(color);
        setSize(width, height);

        this.WIDTH = width;
        this.HEIGHT = height;
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // Agregar KeyListener para manejar el movimiento del cubo
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> traslacion(-10, 0, 0);
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> traslacion(10, 0, 0);
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> traslacion(0, -10, 0);
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> traslacion(0, 10, 0);
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    public static JFrame generarJFrame(int width, int height) {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D - Traslación Paralela");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, this);
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        drawCube(coordenada, zPlano, size);
    }

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color c) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        int step_Points = Math.max(Math.abs(dx), Math.abs(dy));

        float xInc = (float) dx / step_Points;
        float yInc = (float) dy / step_Points;

        float x = x0;
        float y = y0;

        for (int i = 0; i <= step_Points; i++) {
            putPixel(Math.round(x), Math.round(y), c);
            x += xInc;
            y += yInc;
        }
    }

    public void drawCube(int[] point, int zPlano, int size) {
        int x = point[0];
        int y = point[1];
        int z = point[2];
    
        int[][] vertex = {
            {x, y, z},
            {x + size, y, z},
            {x + size, y + size, z},
            {x, y + size, z},
            {x, y, z + size},
            {x + size, y, z + size},
            {x + size, y + size, z + size},
            {x, y + size, z + size}
        };

        int[][] put_Vertex = new int[8][2];
        int xp = 1;
        int yp = 1;
        int zp = zPlano;

        for (int i = 0; i < vertex.length; i++) {
            int x1 = vertex[i][0];
            int y1 = vertex[i][1];
            int z1 = vertex[i][2];
            
            double u = -(z1 / (double) zp);
            put_Vertex[i][0] = (int) (x1 + xp * u);
            put_Vertex[i][1] = (int) (y1 + yp * u);
        }

        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            int x0 = put_Vertex[edge[0]][0];
            int y0 = put_Vertex[edge[0]][1];
            int x1 = put_Vertex[edge[1]][0];
            int y1 = put_Vertex[edge[1]][1];
            drawLine(x0 + WIDTH / 2, y0 + HEIGHT / 2, x1 + WIDTH / 2, y1 + HEIGHT / 2, Color.RED);
        }
    }

    private void traslacion(int dx, int dy, int dz) {
        // Aplicar la matriz de traslación a las coordenadas del cubo
        coordenada[0] += dx;
        coordenada[1] += dy;
        coordenada[2] += dz;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = generarJFrame(700, 580);
        TraslacionCuboParalelo_3D panel = new TraslacionCuboParalelo_3D(Color.BLACK, 700, 580);
        frame.add(panel);
        frame.setVisible(true);
    }
}
