package com.brian.flappybird;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Renderer extends JPanel implements KeyListener, ActionListener{

    final int WIDTH = 525, HEIGHT = 550;
    final int WALLVELOCITY = 5;
    final int WALLWIDTH = 50;
    int flappyHeight = HEIGHT/4;
    int velocity = 0, acceleration = 8, impulse = 1 ;
    int[] wallX = {WIDTH, WIDTH+WIDTH/2};
    int[] gap = {(int)(Math.random()* (HEIGHT-150)), (int)(Math.random()* (HEIGHT-100))};
    boolean gameOver = false;
    int score = 0;
    Timer time = new Timer(40, this);

    public Renderer(){
        setSize(WIDTH,HEIGHT);
        setFocusable(true);
        addKeyListener(this);

        setBackground(Color.BLACK);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!gameOver){
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + score, WIDTH/2,10);
            drawWall(g);
            logic();
            drawFlappy(g);
        }else{
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + score, WIDTH/2,10);
        }
    }

    private void drawFlappy(Graphics g){

        g.setColor(Color.WHITE);

        g.fillRect(150, flappyHeight + velocity, 25,25);
    }

    private void drawWall(Graphics g){

        for (int i = 0; i < 2; i++) {

            g.setColor(Color.RED);
            g.fillRect(wallX[i], 0, WALLWIDTH, HEIGHT);

            g.setColor(Color.BLACK);
            g.fillRect(wallX[i], gap[i], WALLWIDTH, 100);
        }
    }

    private void logic(){
        for (int i = 0; i < 2; i++) {
            if (wallX[i] <= 100 && wallX[i] + WALLWIDTH >= 100 || wallX[i] <= 75 && wallX[i] * WALLWIDTH >=75){
                if ((flappyHeight + velocity) >= 0 && (flappyHeight + velocity) <= gap[i]
                        || (flappyHeight + velocity + 25) >= gap[i] + 100 && (flappyHeight + velocity + 25) <= HEIGHT) {
                    gameOver = true;
                }
            }

            if(flappyHeight+velocity<=0 || flappyHeight+velocity+25>= HEIGHT){
                gameOver = true;
            }

            if(75 > wallX[i] + WALLWIDTH){
                score++;
            }

            if(wallX[i]+WALLWIDTH<=0){
                wallX[i] = WIDTH;
                gap[i] = (int)(Math.random()* (HEIGHT-150));
                }
            }
        }


    public void actionPerformed(ActionEvent e){

        acceleration+=impulse;
        velocity += acceleration;
        wallX[0] -= WALLVELOCITY;
        wallX[1] -= WALLVELOCITY;

        repaint();
    }



    public void keyTyped(KeyEvent e){

    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == e.VK_SPACE) {
            acceleration = -10;
        }

        if (code == e.VK_S) {
            time.start();

        }

        if (code == e.VK_R) {
            time.stop();
            flappyHeight = HEIGHT / 4;
            velocity = 0;
            acceleration = 8;
            score = 0;
            wallX[0] = WIDTH;
            wallX[1] = WIDTH + WIDTH / 2;
            gap[0] = (int) (Math.random() * (HEIGHT - 150));
            gap[1] = (int) (Math.random() * (HEIGHT - 150));
            gameOver = false;

            repaint();
        }
    }

    public void keyReleased(KeyEvent e){

    }

}
