import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class Rocketeer extends JComponent implements ActionListener {
    static final int screenWidth = 960;
    static final int screenHeight = 720;

    int fps = 120;
    int bufferedTime = Math.round((1000 / fps));

    String title = "Rocketeer";

    Timer gameTimer;

    //

    double playerX = 0;
    double playerY = screenHeight / 2;

    boolean up;
    boolean right;
    boolean left;

    double yBoost = 0.1;
    double xBoost = 0.1;
    double horBoost = 1;

    double xAcceleration;
    double yAcceleration;
    double gravityForce = 0.25;
    //double gravityForce = 0;

    int maxAcceletation = 10;

    int playerSize = 50;

    double rotation = 0;

    //

    public Rocketeer() {
        JFrame frame = new JFrame(title);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        frame.add(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(new Keyboard());
        gameTimer = new Timer(bufferedTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();

        setup();
    }

    @Override
    public void paintComponent(Graphics g) {
        // paint background
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);
        // paint player
        g.setColor(Color.gray);
        g.drawPolygon(triginometry(toRadian(rotation), true, playerX, playerY), triginometry(toRadian(rotation), false, playerX, playerY), 4);
    }

    public double toRadian(Double angle){
        return ((180 + angle) *(Math.PI / 180));
    }

    public int[] triginometry(Double rotation, boolean type, double oX, double oY){
        int[] polygon = new int[4];
        double aX = playerSize / 2 + oX + (-25 * Math.cos(rotation)) - (0 * Math.sin(rotation));
        double bX = playerSize / 2 + oX + (0 * Math.cos(rotation)) - (50 * Math.sin(rotation));
        double cX = playerSize / 2 + oX + (25 * Math.cos(rotation)) - (0 * Math.sin(rotation));
        double dX = playerSize / 2 + oX + (0 * Math.cos(rotation)) - (25 * Math.sin(rotation));

        double aY = playerSize + oY + (-25 * Math.sin(rotation)) + (0 * Math.cos(rotation));
        double bY = playerSize + oY + (0 * Math.sin(rotation)) + (50 * Math.cos(rotation));
        double cY = playerSize + oY + (25 * Math.sin(rotation)) + (0 * Math.cos(rotation));
        double dY = playerSize + oY + (0 * Math.sin(rotation)) + (25 * Math.cos(rotation));

        if (type){
            polygon[0] = (int)aX;
            polygon[1] = (int)bX;
            polygon[2] = (int)cX;
            polygon[3] = (int)dX;
        } else {
            polygon[0] = (int)aY;
            polygon[1] = (int)bY;
            polygon[2] = (int)cY;
            polygon[3] = (int)dY;
        }
        return polygon;
    }

    public void update() {
        // boost
        if (up) {
            if (yAcceleration - yBoost > maxAcceletation * -1 && xAcceleration - xBoost > maxAcceletation * -1) {
                yBoost = 0.5 * Math.cos(toRadian(rotation));
                xBoost = 0.5 * Math.sin(toRadian(rotation));
                yAcceleration = yAcceleration + yBoost;
                xAcceleration = xAcceleration - xBoost;
            }
        }
        if (right){
            rotation = rotation + horBoost;
        }
        if (left){
            rotation = rotation - horBoost;
        }
        // apply acceleration
        if (playerY + yAcceleration < screenHeight && playerY + yAcceleration >= (playerSize * -1) - playerSize / 2) {
            playerX = playerX + xAcceleration;
            playerY = playerY + yAcceleration;
            
        }
        // gravity
        if (playerY + yAcceleration + playerSize + gravityForce < screenHeight) {
            if (yAcceleration + gravityForce < maxAcceletation && yAcceleration - yAcceleration > maxAcceletation * -1) {
                yAcceleration = yAcceleration + gravityForce;
            }
        }

        xAcceleration = xAcceleration - 0.025 * xAcceleration;
        yAcceleration = yAcceleration - 0.025 * yAcceleration;

        // check if player is beyond bounds
        if (playerY + playerSize > screenHeight) {
            playerY = screenHeight - playerSize;
        }
        if (playerX + playerSize > screenWidth){
            playerX = screenWidth - playerSize;
        }
        if (playerY < 0){
            playerY = 0;
        }
        if (playerX < 0){
            playerX = 0;
        }
    }

    public void setup() {
        playerX = screenWidth / 2 - playerSize / 2;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        update();
        repaint();
    }

    private class Keyboard extends KeyAdapter {
        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W) {
                up = true;
            }
            if (key == KeyEvent.VK_D){
                right = true;
            }
            if (key == KeyEvent.VK_A){
                left = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W) {
                up = false;
            }
            if (key == KeyEvent.VK_D){
                right = false;
            }
            if (key == KeyEvent.VK_A){
                left = false;
            }
        }
    }

    public static void main(String[] args) {
        Rocketeer game = new Rocketeer();
    }
}
