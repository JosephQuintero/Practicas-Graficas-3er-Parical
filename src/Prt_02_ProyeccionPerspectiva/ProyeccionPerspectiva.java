//Practica 02 Proyeccion de Perspectiva
//Joseph Ivan Quintero Carrasco

package Prt_02_ProyeccionPerspectiva;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ProyeccionPerspectiva extends JPanel {

    private BufferedImage buffer;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int OBSERVER_X = WIDTH / 2;
    private static final int OBSERVER_Y = HEIGHT / 2;
    private static final int OBSERVER_Z = 50;
    private static final int FOCAL_LENGTH = 90;

    private final int[][] vertices = new int[][]{
        {50, 50, 50}, // A
        {150, 50, 50}, // B
        {150, 150, 50}, // C
        {50, 150, 50}, // D
        {50, 50, 150}, // E
        {150, 50, 150}, // F
        {150, 150, 150},// G
        {50, 150, 150} // H
    };
    
    private final int[][] edges = new int[][]{
        {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Aristas del cuadrado frontal
        {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Aristas del cuadrado trasero
        {0, 4}, {1, 5}, {2, 6}, {3, 7} // Aristas que conectan los cuadrados
    };

    public ProyeccionPerspectiva() {
        setSize(WIDTH, HEIGHT);
        setBackground(Color.BLACK);
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        dibujarCuboPerspectiva();
    }

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public static JFrame generarJFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D - Proyección Perspectiva");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        // Algoritmo de Bresenham para dibujar líneas
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (x1 != x2 || y1 != y2) {
            putPixel(x1, y1, color);
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    private void dibujarCuboPerspectiva() {
        int[][] proyectarVertices = new int[vertices.length][2];
        for (int i = 0; i < vertices.length; i++) {
            proyectarVertices[i] = proyeccionVertice(vertices[i][0], vertices[i][1], vertices[i][2]);
        }

        Graphics g = buffer.getGraphics();
        
        for (int[] edge : edges) {
            int startVertexIndex = edge[0];
            int endVertexIndex = edge[1];
            int startX = proyectarVertices[startVertexIndex][0];
            int startY = proyectarVertices[startVertexIndex][1];
            int endX = proyectarVertices[endVertexIndex][0];
            int endY = proyectarVertices[endVertexIndex][1];
            drawLine(g, startX, startY, endX, endY, Color.RED);
        }
    }

    private int[] proyeccionVertice(int x, int y, int z) {
        return new int[]{
            (FOCAL_LENGTH * x) / (z + OBSERVER_Z) + OBSERVER_X,
            (FOCAL_LENGTH * y) / (z + OBSERVER_Z) + OBSERVER_Y
        };
    }

    public static void main(String[] args) {
        JFrame frame = generarJFrame();
        frame.add(new ProyeccionPerspectiva());
        frame.setVisible(true);
    }
}
