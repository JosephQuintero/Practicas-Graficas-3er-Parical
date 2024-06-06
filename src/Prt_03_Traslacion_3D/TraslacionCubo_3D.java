//Joseph Ivan Quintero Carrasco

package Prt_03_Traslacion_3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TraslacionCubo_3D extends JPanel implements KeyListener {

    TraslacionCompleta buffer = new TraslacionCompleta(700, 580);

    private double[] puntoCubo = {200, 200, 0};
    private double[] puntoFuga = {350, 250, 500};

    private double traslacionX = 0;
    private double traslacionY = 0;
    private double traslacionZ = 0;
    private int escala = 40;

    private double[][] vertices = {
        {-1, -1, -1},   //Vertice 1
        {1, -1, -1},    //Vertice 2
        {1, 1, -1},     //Vertice 3
        {-1, 1, -1},    //Vertice 4
        {-1, -1, 1},    //Vertice 5
        {1, -1, 1},     //Vertice 6
        {1, 1, 1},      //Vertice 7
        {-1, 1, 1}      //Vertice 8
    };

    //Dibujamos las coordenadas que estarán conectadas a cada vertice
    private int[][] edges = {
        {0, 1}, {1, 2}, {2, 3}, {3, 0},
        {4, 5}, {5, 6}, {6, 7}, {7, 4},
        {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    public TraslacionCubo_3D() {
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    public static JFrame generarFrame(JPanel panel) {
        JFrame frame = new JFrame();
        frame.setTitle("Traslación del Cubo Perspectiva");
        frame.setSize(700, 580);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
        return frame;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Declaramos el arreglo para almacenar las coordenadas de vertices trasladados del cubo.
        double[][] verticesTrasladados = new double[8][3];
        for (int i = 0; i < vertices.length; i++) {
            double[] v = vertices[i];
            double[] trasladado = {
                (v[0] * escala) + traslacionX + puntoCubo[0],
                (v[1] * escala) + traslacionY + puntoCubo[1],
                (v[2] * escala) + traslacionZ + puntoCubo[2]
            };
            verticesTrasladados[i] = trasladado;
        }
        for (int[] edge : edges) {
            double x0 = verticesTrasladados[edge[0]][0];
            double y0 = verticesTrasladados[edge[0]][1];
            double z0 = verticesTrasladados[edge[0]][2];

            double x1 = verticesTrasladados[edge[1]][0];
            double y1 = verticesTrasladados[edge[1]][1];
            double z1 = verticesTrasladados[edge[1]][2];

            Point2D p1 = trasnformacionPuntos(x0, y0, z0);
            Point2D p2 = trasnformacionPuntos(x1, y1, z1);

            buffer.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), Color.RED);
        }

        // Dibujar el punto de fuga
        drawPoint(puntoFuga[0], puntoFuga[1], Color.GREEN);

        // Dibujar el punto del cubo
        drawPoint(puntoCubo[0], puntoCubo[1], Color.YELLOW);

        g.drawImage(buffer.getBuffer(), 0, 0, null);
        buffer.resetBuffer();
    }

    private Point2D trasnformacionPuntos(double x, double y, double z) {
        //Generamos las formulas necesarias para aplicar la traslacion del objeto
        double u = -puntoFuga[2] / (z - puntoFuga[2]);

        double px = puntoFuga[0] + (x - puntoFuga[0]) * u;
        double py = puntoFuga[1] + (y - puntoFuga[1]) * u;

        return new Point2D.Double(px, py);
    }

    private void drawPoint(double x, double y, Color color) {
        buffer.putPixel((int) x, (int) y, color);
    }
    
    private void traslacion(double dx, double dy, double dz) {
        traslacionX += dx;
        traslacionY += dy;
        traslacionZ += dz;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> traslacion(-10, 0, 0);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> traslacion(10, 0, 0);
            case KeyEvent.VK_W, KeyEvent.VK_UP -> traslacion(0, -10, 0);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> traslacion(0, 10, 0);
            case KeyEvent.VK_E -> traslacion(0, 0, 10);
            case KeyEvent.VK_Q -> traslacion(0, 0, -10);
            default -> {
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita implementación
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita implementación
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TraslacionCubo_3D panel = new TraslacionCubo_3D();
            generarFrame(panel);
        });
    }
}

class TraslacionCompleta {

    private BufferedImage buffer;
    private int WIDTH;
    private int HEIGHT;

    public TraslacionCompleta(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        int pasos = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncremento = (float) dx / pasos;
        float yIncremento = (float) dy / pasos;
        float x = x0;
        float y = y0;

        for (int i = 0; i <= pasos; i++) {
            putPixel(Math.round(x), Math.round(y), color);
            x += xIncremento;
            y += yIncremento;
        }
    }

    public void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    public void resetBuffer() {
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getBuffer() {
        return buffer;
    }
}

