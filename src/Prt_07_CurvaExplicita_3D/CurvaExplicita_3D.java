//Joseph Ivan Quintero Carrasco

package Prt_07_CurvaExplicita_3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CurvaExplicita_3D extends JPanel implements KeyListener, Runnable {

    private BufferedImage buffer;
    private int WIDTH = 700;
    private int HEIGHT = 580;
    private boolean animacionActiva = false;
    private double anguloX = 0;
    private double anguloY = 0;
    private double anguloZ = 0;

    private ArrayList<double[]> curvaVertices = new ArrayList<>();
    private double[] puntoFuga = {WIDTH / 2.0, HEIGHT / 2.0, 1000};

    public CurvaExplicita_3D() {
        this.setFocusable(true);
        this.addKeyListener(this);
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        generarCurva();
        JFrame frame = generarFrame();
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < curvaVertices.size() - 1; i++) {
            double[] punto1 = rotar(curvaVertices.get(i), anguloX, anguloY, anguloZ);
            double[] punto2 = rotar(curvaVertices.get(i + 1), anguloX, anguloY, anguloZ);

            Point2D p1 = translateToCenter(punto3D_a_2D(punto1[0], punto1[1], punto1[2]));
            Point2D p2 = translateToCenter(punto3D_a_2D(punto2[0], punto2[1], punto2[2]));

            Color color = arcoiris[i % arcoiris.length];
            drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), color);
        }

        g.drawImage(buffer, 0, 0, this);
    }

    public static JFrame generarFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Curva 3D con Rotación");
        frame.setSize(700, 580);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
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

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    private void generarCurva() {
        double t = 0;
        while (t < 10 * Math.PI) {
            double x = 50 * Math.cos(t);
            double y = 50 * Math.sin(t);
            double z = 10 * t;
            curvaVertices.add(new double[]{x, y, z});
            t += 0.1;
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
        SwingUtilities.invokeLater(CurvaExplicita_3D::new);
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
