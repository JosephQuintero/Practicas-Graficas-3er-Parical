package Prt_02_ProyeccionPerspectiva;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ProyeccionPerspectivaCubo extends JPanel {

    private BufferedImage buffer;
    private int WIDTH;
    private int HEIGHT;
    private double[] puntoFuga = {50, 50, 600};

    private final double escala = 70;

    private final double[][] vertices = {
        {-1, -1, -1},
        {1, -1, -1},
        {1, 1, -1},
        {-1, 1, -1},
        {-1, -1, 1},
        {1, -1, 1},
        {1, 1, 1},
        {-1, 1, 1}
    };
    
    private final int[][] edges = {
        {0, 1}, {1, 2}, {2, 3}, {3, 0},
        {4, 5}, {5, 6}, {6, 7}, {7, 4},
        {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    public ProyeccionPerspectivaCubo(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        resetBuffer();
        setBackground(Color.BLACK); 
    }

    public void resetBuffer() {
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCube3D(g);
    }

    private Point2D puntos3D_a_2D(double x, double y, double z) {
        double fov = 500;
        double u = -fov / (z - fov);

        double px = (x * u) + getWidth() / 2;
        double py = (y * u) + getHeight() / 2;

        return new Point2D.Double(px, py);
    }

    public void drawCube3D(Graphics g) {
        double[][] verticesRotados = rotarVertices();

        for (int[] edge : edges) {
            double x0 = verticesRotados[edge[0]][0] * escala;
            double y0 = verticesRotados[edge[0]][1] * escala;
            double z0 = verticesRotados[edge[0]][2] * escala;

            double x1 = verticesRotados[edge[1]][0] * escala;
            double y1 = verticesRotados[edge[1]][1] * escala;
            double z1 = verticesRotados[edge[1]][2] * escala;

            Point2D p1 = puntos3D_a_2D(x0, y0, z0);
            Point2D p2 = puntos3D_a_2D(x1, y1, z1);

            drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), g);
        }

        // Dibujar líneas desde el punto de fuga a cada vértice
        Point2D puntoFuga2D = puntos3D_a_2D(puntoFuga[0], puntoFuga[1], puntoFuga[2]);
        for (double[] vertice : verticesRotados) {
            double x = vertice[0] * escala;
            double y = vertice[1] * escala;
            double z = vertice[2] * escala;

            Point2D verticeProyectado = puntos3D_a_2D(x, y, z);
            drawLine((int) puntoFuga2D.getX(), (int) puntoFuga2D.getY(),
                    (int) verticeProyectado.getX(), (int) verticeProyectado.getY(), g);
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(x0, y0, x1, y1);
    }

    private double[][] rotarVertices() {
        double theta = Math.toRadians(30); // Ángulo de rotación en radianes
        double[][] verticesRotados = new double[vertices.length][vertices[0].length];

        // Matriz de rotación 3D en el plano XY
        double[][] matrizRotacion = {
            {Math.cos(theta), 0, Math.sin(theta)},
            {0, 1, 0},
            {-Math.sin(theta), 0, Math.cos(theta)}
        };

        // Aplicar la rotación a cada vértice
        for (int i = 0; i < vertices.length; i++) {
            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];

            // Realizar la multiplicación de matrices
            verticesRotados[i][0] = matrizRotacion[0][0] * x + matrizRotacion[0][1] * y + matrizRotacion[0][2] * z;
            verticesRotados[i][1] = matrizRotacion[1][0] * x + matrizRotacion[1][1] * y + matrizRotacion[1][2] * z;
            verticesRotados[i][2] = matrizRotacion[2][0] * x + matrizRotacion[2][1] * y + matrizRotacion[2][2] * z;
        }

        return verticesRotados;
    }

    public static JFrame generarJFrame(int width, int height) {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D - Proyección en Perspectiva");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    public static void main(String[] args) {
        JFrame frame = generarJFrame(700, 580);

        // Crear el panel y configurar su tamaño
        ProyeccionPerspectivaCubo panel = new ProyeccionPerspectivaCubo(700, 580);
        panel.setBounds(0, 0, 700, 580); // Ajustar el tamaño del panel al tamaño del JFrame

        frame.add(panel);
        frame.setVisible(true);
    }
}
