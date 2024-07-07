package research;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PongTest extends JFrame implements ActionListener {
    private int previousBallX;
    private int previousBallY;

    private int ballX = 200;
    private int ballY = 150;

    private double ballXSpeed = 2;
    private double ballYSpeed = 2;

    private int rectangleX = WIDTH / 4;
    private int rectangleY = HEIGHT / 4;
    private int rectangleWidth = 100;
    private int rectangleHeight = 50;

    private double BALL_RADIUS=.1;

    private Timer timer;

    public PongTest() {
        previousBallX=ballX;
        previousBallY=ballY;

        setTitle("Circular Pong");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        timer = new Timer(10, this);
        timer.start();

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Update ball position based on direction and speed
        previousBallX=ballX;
        previousBallY=ballY;

        // Update ball position based on direction and speed
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        if (ballX + BALL_RADIUS >= rectangleX &&
                ballX - BALL_RADIUS <= rectangleX + rectangleWidth &&
                ballY + BALL_RADIUS >= rectangleY &&
                ballY - BALL_RADIUS <= rectangleY + rectangleHeight) {
            ballX -= ballXSpeed;
            ballY -= ballYSpeed;       // Reverse ball direction on collision
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        double distanceToCenter = Math.sqrt(Math.pow(ballX - getWidth() / 2, 2) + Math.pow(ballY - getHeight() / 2, 2));

        if (distanceToCenter >= getWidth() / 2){
            ballX -= ballXSpeed;
            ballY -= ballYSpeed;

            ballXSpeed = Math.random()*100+1;
            ballYSpeed = Math.random()*100+1;

            if (Math.random()<.5){
                ballXSpeed*=-1;
            }

            if (Math.random()<.5){
                ballYSpeed*=-1;
            }
        }

        repaint();
    }

    public void paint(Graphics g) {
        //  super.paint(g);
        drawBall(g);
    }

    private void drawBall(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(previousBallX, previousBallY, ballX, ballY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PongTest());
    }
}