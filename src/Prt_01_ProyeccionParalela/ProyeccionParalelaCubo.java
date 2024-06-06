//Practica 01 Proyeccion Paralela
//Joseph Ivan Quintero Carrasco

package Prt_01_ProyeccionParalela;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ProyeccionParalelaCubo extends JPanel {

    BufferedImage buffer;
    private int WIDTH;
    private int HEIGHT;

    public ProyeccionParalelaCubo(Color color, int width, int height) {
        setBackground(color);
        setSize(width, height);

        this.WIDTH = width;
        this.HEIGHT = height;
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public static JFrame generarJFrame(int width, int height) {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D - Proyeccion Paralela");
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
    }

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color c) {
        // Calculamos las diferencias con los respectivos ejes
        int dx = x1 - x0;
        int dy = y1 - y0;

        // Determinamos el número de pasos para dibujar la línea
        int step_Points = Math.max(Math.abs(dx), Math.abs(dy));

        // Calculamos el incremento deseado
        float xInc = (float) dx / step_Points;
        float yInc = (float) dy / step_Points;

        // Coordenadas iniciales
        float x = x0;
        float y = y0;

        // Dibujamos los puntos sobre la línea
        for (int i = 0; i <= step_Points; i++) {
            putPixel(Math.round(x), Math.round(y), c);
            x += xInc;
            y += yInc;
        }
    }

    public void drawCube(int[] point, int zPlano, int size) {
        //Inicialización de Coordenadas
        int x = point[0];
        int y = point[1];
        int z = point[2];
    
        //Definimos los vértices que tendrá el cubo TOTAL = 8
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

        //Proyectamos los vértices en 2D
        int[][] put_Vertex = new int[8][2];
        int xp = 1; //Proyeccion en X
        int yp = 1; //Proyeccion en Y
        int zp = zPlano; //Profundidad del cubo en proyeccion

        for (int i = 0; i < vertex.length; i++) {
            int x1 = vertex[i][0];
            int y1 = vertex[i][1];
            int z1 = vertex[i][2];
            
            //Operaciones para la proyeccion Paralela Simple
            double u = -(z1 / (double) zp);//Calculamos el factor de U
            put_Vertex[i][0] = (int) (x1 + xp * u);
            put_Vertex[i][1] = (int) (y1 + yp * u);
        }

        //Definimos las aristas en 2D
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        //Proyectamos las aristas en 2D
        for (int[] edge : edges) {
            int x0 = put_Vertex[edge[0]][0];
            int y0 = put_Vertex[edge[0]][1];
            int x1 = put_Vertex[edge[1]][0];
            int y1 = put_Vertex[edge[1]][1];
            drawLine(x0 + WIDTH / 2, y0 + HEIGHT / 2, x1 + WIDTH / 2, y1 + HEIGHT / 2, Color.RED);
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = generarJFrame(700, 580);

        //Parametros para poder dibujar el Cubo
        ProyeccionParalelaCubo panel = new ProyeccionParalelaCubo(Color.BLACK, 700, 580);
        frame.add(panel);
        frame.setVisible(true);

        int[] coordenada = {-100, -100, -100};
        int zPlano = 2;
        panel.drawCube(coordenada, zPlano, 200);
    }
}
