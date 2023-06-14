package com.brian.flappybird;


import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener , MouseListener, KeyListener
{

    public static FlappyBird flappyBird;
    public final int WIDTH = 800, HEIGHT = 800;
    public flappyPanel flappyPanel;
    public Rectangle bird;
    public int ticks, yMotion, score;
    public ArrayList<Rectangle> columns;
    public boolean gameOver, started;
    public Random rand;





    public FlappyBird()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(25, this); // adjusts delay, speeding up or slowing down the movement on screen
        flappyPanel = new flappyPanel();
        rand = new Random(); //  random number generator in order to randomize the hieght of the gaps in columns

        jframe.add(flappyPanel);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setTitle("Flappy Bird");
        jframe.setResizable(false);
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20); // gives bird a starting position on the screen and size
        columns = new ArrayList<Rectangle>(); // initializes a new array of 4 columns

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start(); // starts the timer and the game
    }



    public void addColumn(boolean start)
    {
        int space = 300; // adjusts the size of the gap
        int width = 100; // width of the columns
        int height = 50 + rand.nextInt(300);  // adjusts the vertical range of which the gap can be randomly selected on the columns
                                                    // the smaller the number the less the gap will vary in position vertically

        if (start) // this section of code is responsible for creating and positioning rectangles (columns)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));     // when the game is started it initializes the first 4 columns
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));// when the game is started it initializes the first 4 columns
        }
        else                                                                                                           // columns are represented as such (int x, int y, int width, int height)
                                                                                                                        // this determines the position and space between them
        {
            columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, HEIGHT - height - 120, width, height)); //continues the columns after the intial 4 with 'start'
            columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - space));  //continues the columns after the intial 4 with 'start'
        }

    }

    public void paintColumn(Graphics g, Rectangle column) // colors the columns green
    {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() // logic for the event listener
    {
        if(gameOver)
        {
            bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20); //resets bird position
            columns.clear(); //clears columns
            yMotion = 0; // stops the ymotion from being able to 'fly' the bird
            score = 0; // resets score to zero

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false; // resets the game to start so its no longer game over
        }
        if(!started) //automatically has the game restart upon reset
        {
            started = true;
        }
        else if (!gameOver) // if its not game over and you havent just started then the event listener will 'fly' the bird
        {
           if(yMotion>0){
               yMotion = 0;
           }
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int speed = 10;
        ticks++;

        if(started)
        {
            for (Rectangle column : columns) { //It decreases the x-coordinate of each column by the speed value, causing the rectangles to move towards the left side of the screen.
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) { //It increases the yMotion value by 2 every second tick (when ticks is divisible by 2), up to a maximum value of 15. This controls the vertical motion of the game object (bird).
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) { // once column has moved entirely off the screen (column.x + column.width < 0), it is removed from the columns list. If the removed column's y value is 0, a new column is added using the addColumn method with start set to false.
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }
            bird.y += yMotion; //updates the position of the bird object by adding the current yMotion value to its y-coordinate, controlling its vertical motion

            for(Rectangle column : columns)
            {
                if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width/ 2 - 10 //If the bird passes between the columns, the score is incremented.
                        && bird.x + bird.width / 2 < column.x + column.width /2 +10)
                {
                    score++;
                }

                if(column.intersects(bird)) //checks if the bird ever 'hits the column
                {
                    gameOver = true; // triggers game over

                    if (bird.x <= column.x) //If the bird's x-coordinate (horizontal axis) is less than or equal to the column's x-coordinate, the bird is positioned to the left of the column.
                    {
                        bird.x = column.x - bird.width; // stops the bird from moving forward 'through' the columns if they intersect
                    }
                    else
                    {
                        if (column.y != 0) // same as above but instead of stopping the bird from passing through the side of the column,
                        {                   //it stops it from passing through the flat top of the bottom column or flat bottom of the top column.
                            bird.y = column.y - bird.height;
                        }
                        else if (bird.y < column.height)
                        {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if(bird.y > HEIGHT-140 || bird.y < 0) // if the bird goes above the top of the screen or touches the ground game over is triggered.
            {

                gameOver = true;
            }
            if (bird.y + yMotion >= HEIGHT -120) // stops 'bird' from falling through the ground and off-screen.
            {
                bird.y = HEIGHT-120 - bird.height;
            }
        }

        flappyPanel.repaint(); // updates visuals by calling on the repaint method
    }

    public void repaint(Graphics g)
    {
        g.setColor(Color.cyan);
        g.fillRect(0,0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0,HEIGHT-120, WIDTH, 120);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT-120, WIDTH, 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column : columns)
        {
            paintColumn(g, column); // calls on the paint columns method
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1,100));

        if(!started)
        {
            g.drawString("Click/Space to Start!",100, HEIGHT/2 - 50);
        }

        if(gameOver)
        {
            g.drawString("Game Over!",100, HEIGHT/2 - 50);
        }

        if (!gameOver && started){
            g.drawString(String.valueOf(score),WIDTH / 2 -25, 100);
        }
    }

    public static void main(String[] args)
    {
        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }
}



