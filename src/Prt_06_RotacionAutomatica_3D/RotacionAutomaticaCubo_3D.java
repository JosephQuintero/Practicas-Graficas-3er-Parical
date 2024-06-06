
package Prt_06_RotacionAutomatica_3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RotacionAutomaticaCubo_3D extends JPanel {
    
    private BufferedImage buffer;
    private double[][] vertices;
    private int[][] edges;
    private double anguloX, anguloY, anguloZ;
    private final double escala = 60;

    private final double[][] puntoCubo = {{350, 250, 0}};
    private final double[][] puntoFuga = {{200, 250, 500}};

    public RotacionAutomaticaCubo_3D() {
        setBackground(Color.BLACK);
        generarCubo();
        
        // Iniciar la rotación automática
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(50); // Pausa de 50 milisegundos
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                anguloX += Math.PI / 180; // Rotación en X (1 grado)
                anguloY += Math.PI / 180; // Rotación en Y (1 grado)
                anguloZ += Math.PI / 180; // Rotación en Z (1 grado)
                rotar(anguloX, anguloY, anguloZ);
            }
        };
        new Thread(runnable).start();
    }

    public static JFrame generarFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D con Rotación Automática");
        frame.setSize(700, 580);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RotacionAutomaticaCubo_3D());
        return frame;
    }

    public void generarCubo() {
        vertices = new double[][]{
            {-1, -1, -1}, //Vertice 1
            {1, -1, -1}, //Vertice 2
            {1, 1, -1}, //Vertice 3
            {-1, 1, -1}, //Vertice 4
            {-1, -1, 1}, //Vertice 5
            {1, -1, 1}, //Vertice 6
            {1, 1, 1}, //Vertice 7
            {-1, 1, 1} //Vertice 8
        };

        edges = new int[][]{
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };
    }

    private void drawLine(BufferedImage buffer, int x0, int y0, int x1, int y1, Color color) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;
        float x = x0;
        float y = y0;

        for (int i = 0; i <= steps; i++) {
            putPixel(buffer, Math.round(x), Math.round(y), color);
            x += xIncrement;
            y += yIncrement;
        }
    }

    private void putPixel(BufferedImage buffer, int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    private void rotar(double angX, double angY, double angZ) {
        // Rotación en el eje X
        double[][] rotX = {
            {1, 0, 0},
            {0, Math.cos(angX), -Math.sin(angX)},
            {0, Math.sin(angX), Math.cos(angX)}
        };

        // Rotación en el eje Y
        double[][] rotY = {
            {Math.cos(angY), 0, Math.sin(angY)},
            {0, 1, 0},
            {-Math.sin(angY), 0, Math.cos(angY)}
        };

        // Rotación en el eje Z
        double[][] rotZ = {
            {Math.cos(angZ), -Math.sin(angZ), 0},
            {Math.sin(angZ), Math.cos(angZ), 0},
            {0, 0, 1}
        };

        // Aplicar rotaciones a cada vértice
        for (int i = 0; i < vertices.length; i++) {
            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];
            // Rotar en Z -> Y -> X
            double newX = rotX[0][0]*x + rotX[0][1]*y + rotX[0][2]*z;
            double newY = rotX[1][0]*x + rotX[1][1]*y + rotX[1][2]*z;
            double newZ = rotX[2][0]*x + rotX[2][1]*y + rotX[2][2]*z;
            
            x = newX;
            y = newY;
            z = newZ;
            
            newX = rotY[0][0]*x + rotY[0][1]*y + rotY[0][2]*z;
            newY = rotY[1][0]*x + rotY[1][1]*y + rotY[1][2]*z;
            newZ = rotY[2][0]*x + rotY[2][1]*y + rotY[2][2]*z;
            
            x = newX;
            y = newY;
            z = newZ;
            
            newX = rotZ[0][0]*x + rotZ[0][1]*y + rotZ[0][2]*z;
            newY = rotZ[1][0]*x + rotZ[1][1]*y + rotZ[1][2]*z;
            newZ = rotZ[2][0]*x + rotZ[2][1]*y + rotZ[2][2]*z;
            
            vertices[i][0] = newX;
            vertices[i][1] = newY;
            vertices[i][2] = newZ;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Dibujar cubo
        for (int[] edge : edges) {
            double x0 = vertices[edge[0]][0] * escala + puntoCubo[0][0];
            double y0 = vertices[edge[0]][1] * escala + puntoCubo[0][1];
            double z0 = vertices[edge[0]][2] * escala + puntoCubo[0][2];

            double x1 = vertices[edge[1]][0] * escala + puntoCubo[0][0];
            double y1 = vertices[edge[1]][1] * escala + puntoCubo[0][1];
            double z1 = vertices[edge[1]][2] * escala + puntoCubo[0][2];

            Point2D p1 = transformacionPuntos(x0, y0, z0);
            Point2D p2 = transformacionPuntos(x1, y1, z1);

            drawLine(buffer, (int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), Color.RED);
        }

        g.drawImage(buffer, 0, 0, null);
    }

    private Point2D transformacionPuntos(double x, double y, double z) {
        double u = -puntoFuga[0][2] / (z - puntoFuga[0][2]);

        double px = puntoFuga[0][0] + (x - puntoFuga[0][0]) * u;
        double py = puntoFuga[0][1] + (y - puntoFuga[0][1]) * u;

        return new Point2D.Double(px, py);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = generarFrame();
            frame.setVisible(true);
        });
    }
}
