//Practica 01 Proyeccion Paralela
//Joseph Ivan Quintero Carrasco

package Prt_01_ProyeccionParalela;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ProyeccionParalela extends JPanel {

    BufferedImage buffer;
    private int WIDTH;
    private int HEIGHT;

    public ProyeccionParalela(int width, int height, Color c) {
        setSize(width, height);
        setBackground(c);
        this.HEIGHT = height;
        this.WIDTH = width;
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
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

    public static JFrame generarJFrame(int width, int height) {
        JFrame frame = new JFrame();
        frame.setTitle("Cubo 3D - Proyeccion Paralela");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private void drawLine(int x1, int y1, int x2, int y2, Color color) {
        // Verificar si la línea es vertical
        if (x1 == x2) {
            // Dibujar una línea vertical
            int startY = Math.min(y1, y2);
            int endY = Math.max(y1, y2);
            for (int y = startY; y <= endY; y++) {
                putPixel(x1, y, color);
            }
            repaint();
            return;
        }

        // Resto del algoritmo de Bresenham para líneas no verticales
        int incyi, incxi, incyr, incxr, aux, avr, av, avi;
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (dy >= 0) {
            incyi = 1;
        } else {
            dy = -dy;
            incyi = -1;
        }

        if (dx >= 0) {
            incxi = 1;
        } else {
            dx = -dx;
            incxi = -1;
        }

        if (dx >= dy) {
            incyr = 0;
            incxr = incxi;
        } else {
            incxr = 0;
            incyr = incyi;
            aux = dx;
            dx = dy;
            dy = aux;
        }

        int x = x1;
        int y = y1;
        avr = 2 * dy;
        av = avr - dx;
        avi = av - dx;
        do {
            putPixel(x, y, color);
            if (av >= 0) {
                x = x + incxi;
                y = y + incyi;
                av = av + avi;
            } else {
                x = x + incxr;
                y = y + incyr;
                av = av + avr;
            }
        } while (x != x2);

        putPixel(x2, y2, color); // Asegurar que el último píxel se dibuje.
        repaint();
    }

    private void drawCube(int x1, int y1, int lado) {
        int offset = lado / 3;

        // Colores del arcoíris
        Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.BLUE, Color.CYAN, Color.MAGENTA, Color.PINK,
            Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.WHITE
        };

        // Dibujar las aristas del cuadrado frontal
        drawLine(x1, y1, x1 + lado, y1, colors[0]); // Arriba
        drawLine(x1 + lado, y1, x1 + lado, y1 + lado, colors[1]); // Derecha
        drawLine(x1 + lado, y1 + lado, x1, y1 + lado, colors[2]); // Abajo
        drawLine(x1, y1 + lado, x1, y1, colors[3]); // Izquierda

        // Dibujar las aristas del cuadrado trasero
        int x2 = x1 + offset;
        int y2 = y1 + offset;
        drawLine(x2, y2, x2 + lado, y2, colors[4]); // Arriba
        drawLine(x2 + lado, y2, x2 + lado, y2 + lado, colors[5]); // Derecha
        drawLine(x2 + lado, y2 + lado, x2, y2 + lado, colors[6]); // Abajo
        drawLine(x2, y2 + lado, x2, y2, colors[7]); // Izquierda

        // Dibujar las aristas que conectan los dos cuadrados
        drawLine(x1, y1, x2, y2, colors[8]); // Arriba izquierda
        drawLine(x1 + lado, y1, x2 + lado, y2, colors[9]); // Arriba derecha
        drawLine(x1 + lado, y1 + lado, x2 + lado, y2 + lado, colors[10]); // Abajo derecha
        drawLine(x1, y1 + lado, x2, y2 + lado, colors[11]); // Abajo izquierda
    }

    public static void main(String[] args) {
        int width = 500; // Ancho del lienzo
        int height = 500; // Alto del lienzo

        // Crear el JFrame usando la función generarJFrame
        JFrame frame = generarJFrame(width, height);
        ProyeccionParalela panel = new ProyeccionParalela(width, height, Color.BLACK);
        frame.add(panel);

        // Dibujar el cubo
        int x1 = 100; // Coordenadas del primer punto
        int y1 = 100;
        int lado = 200; // Longitud de cada lado del cubo
        
        // Llamar a la función para dibujar el cubo
        panel.drawCube(x1, y1, lado);

        // Mostrar el frame
        frame.setVisible(true);
    }
}
