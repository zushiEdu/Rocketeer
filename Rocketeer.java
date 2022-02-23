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
    double playerY = 0;

    boolean up;

    double boost = 0.5;

    double acceleration;
    double gravityForce = 0.25;

    int maxAcceletation = 10;

    int playerSize = 50;

    double rotation = -1 * (90 * (Math.PI / 180));

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
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.gray);
        int[] xPoints = { (int) playerX + 0, (int) playerX + playerSize / 2, (int) playerX + playerSize,
                (int) playerX + playerSize / 2 };
        int[] yPoints = { (int) playerY + playerSize, (int) playerY + 0, (int) playerY + playerSize,
                (int) playerY + playerSize / 2 };
        g.drawPolygon(xPoints, yPoints, 4);

        // double aX = (-25 * Math.cos(rotation)) - (-25 * Math.sin(rotation));
        // double bX = (0 * Math.cos(rotation)) - (25 * Math.sin(rotation));
        // double cX = (25 * Math.cos(rotation)) - (-25 * Math.sin(rotation));
        // double dX = (0 * Math.cos(rotation)) - (0 * Math.sin(rotation));

        // int[] xPointsRotated = { (int) aX + 50, (int) bX + 50, (int) cX + 50, (int) dX + 50 };

        // double aY = (-25 * Math.sin(rotation)) + (-25 * Math.cos(rotation));
        // double bY = (0 * Math.sin(rotation)) + (25 * Math.cos(rotation));
        // double cY = (25 * Math.sin(rotation)) + (-25 * Math.cos(rotation));
        // double dY = (0 * Math.sin(rotation)) + (0 * Math.cos(rotation));

        // int[] yPointsRotated = { (int) aY + 50, (int) bY + 50, (int) cY + 50, (int) dY + 50 };
        // g.drawPolygon(xPointsRotated, yPointsRotated, 4);
    }

    public void update() {
        // gravity
        if (playerY + acceleration + playerSize + gravityForce < screenHeight) {
            if (acceleration + gravityForce < maxAcceletation && acceleration - acceleration > maxAcceletation * -1) {
                acceleration = acceleration + gravityForce;
            }
        }

        // boost
        if (up) {
            if (acceleration - boost > maxAcceletation * -1) {
                acceleration = acceleration - boost;
            }
        }

        // apply acceleration
        if (playerY + acceleration < screenHeight && playerY + acceleration >= (playerSize * -1) - playerSize / 2) {
            playerY = playerY + acceleration;
        }

        // check if player is beyond bounds
        if (playerY + playerSize > screenHeight) {
            playerY = screenHeight - playerSize;
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
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W) {
                up = false;
            }
        }
    }

    public static void main(String[] args) {
        Rocketeer game = new Rocketeer();
    }
}
