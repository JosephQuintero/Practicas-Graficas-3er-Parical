
package Prt_08_Superficie_3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Superficie_3D_Domo extends JPanel implements KeyListener, Runnable{
    
    private BufferedImage buffer;
    private int WIDTH = 700;
    private int HEIGHT = 580;
    private boolean animacionActiva = false;
    private double anguloX = 0;
    private double anguloY = 0;
    private double anguloZ = 0;
    private final double[] puntoFuga = { 0, 0, 1000 };  // Ajusta el punto de fuga

    public Superficie_3D_Domo() {
        this.setFocusable(true);
        this.addKeyListener(this);
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        JFrame frame = generarFrame();
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics bg = buffer.getGraphics();
        bg.setColor(Color.BLACK);
        bg.fillRect(0, 0, WIDTH, HEIGHT);

        dibujarSuperficie(bg);

        g.drawImage(buffer, 0, 0, null);
    }

    public static JFrame generarFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Superficie 3D con Rotación");
        frame.setSize(700, 580);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private void dibujarSuperficie(Graphics g) {
        int pasos = 100;  // Número de pasos para la malla
        double t_min = -Math.PI;
        double t_max = Math.PI;
        double theta_min = Math.PI;
        double theta_max = 2 * Math.PI;
        double escala = 90;
        
        for (int i = 0; i < pasos; i++) {
            for (int j = 0; j < pasos; j++) {
                double t1 = t_min + i * (t_max - t_min) / pasos;
                double theta1 = theta_min + j * (theta_max - theta_min) / pasos;
                
                double x1 = ((2 + Math.cos(t1)) * Math.cos(theta1)) * escala;
                double y1 = ((2 + Math.cos(t1)) * Math.sin(theta1)) * escala;
                double z1 = (t1) * escala;
                
                double t2 = t_min + (i + 1) * (t_max - t_min) / pasos;
                double theta2 = theta_min + j * (theta_max - theta_min) / pasos;
                
                double x2 = ((2 + Math.cos(t2)) * Math.cos(theta2)) * escala;
                double y2 = ((2 + Math.cos(t2)) * Math.sin(theta2)) * escala;
                double z2 = t2 * escala;

                double[] punto3D1 = { x1, y1, z1 };
                double[] puntoRotado1 = rotar(punto3D1, anguloX, anguloY, anguloZ);
                Point2D punto2D1 = translateToCenter(punto3D_a_2D(puntoRotado1[0], puntoRotado1[1], puntoRotado1[2]));

                double[] punto3D2 = { x2, y2, z2 };
                double[] puntoRotado2 = rotar(punto3D2, anguloX, anguloY, anguloZ);
                Point2D punto2D2 = translateToCenter(punto3D_a_2D(puntoRotado2[0], puntoRotado2[1], puntoRotado2[2]));

                drawLine((int) punto2D1.getX(), (int) punto2D1.getY(), (int) punto2D2.getX(), (int) punto2D2.getY(), arcoiris[Math.abs(i % arcoiris.length)]);
            }
        }
    }

    private void drawLine(int x0, int y0, int x1, int y1, Color color) {
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

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    private Point2D punto3D_a_2D(double x, double y, double z) {
        double u = -puntoFuga[2] / (z - puntoFuga[2]);
        double px = puntoFuga[0] + (x - puntoFuga[0]) * u;
        double py = puntoFuga[1] + (y - puntoFuga[1]) * u;
        return new Point2D.Double(px, py);
    }

    private Point2D translateToCenter(Point2D point) {
        double centerX = WIDTH / 2.0;
        double centerY = HEIGHT / 2.0;
        return new Point2D.Double(point.getX() + centerX, point.getY() + centerY);
    }

    private double[] rotar(double[] punto, double anguloX, double anguloY, double anguloZ) {
        double[] rotadoX = rotarX(punto, anguloX);
        double[] rotadoY = rotarY(rotadoX, anguloY);
        return rotarZ(rotadoY, anguloZ);
    }

    private double[] rotarX(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0];
        result[1] = point[1] * Math.cos(Math.toRadians(angle)) - point[2] * Math.sin(Math.toRadians(angle));
        result[2] = point[1] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    private double[] rotarY(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) + point[2] * Math.sin(Math.toRadians(angle));
        result[1] = point[1];
        result[2] = -point[0] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    private double[] rotarZ(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) - point[1] * Math.sin(Math.toRadians(angle));
        result[1] = point[0] * Math.sin(Math.toRadians(angle)) + point[1] * Math.cos(Math.toRadians(angle));
        result[2] = point[2];
        return result;
    }

    private Color[] arcoiris = {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
        Color.BLUE, new Color(75, 0, 130), new Color(143, 0, 255) // Añadimos Indigo y Violeta
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Superficie_3D_Domo::new);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                anguloX += 2;
                break;
            case KeyEvent.VK_S:
                anguloX -= 2;
                break;
            case KeyEvent.VK_A:
                anguloY -= 2;
                break;
            case KeyEvent.VK_D:
                anguloY += 2;
                break;
            case KeyEvent.VK_E:
                anguloZ += 2;
                break;
            case KeyEvent.VK_Q:
                anguloZ -= 2;
                break;
            case KeyEvent.VK_SPACE:
                animacionActiva = !animacionActiva;
                if (animacionActiva) {
                    new Thread(this).start();
                }
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void run() {
        while (animacionActiva) {
            anguloY += 1;
            anguloZ += 1;
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
