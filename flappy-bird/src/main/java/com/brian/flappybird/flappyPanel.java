package com.brian.flappybird;

import javax.swing.JPanel;
import java.awt.*;


public class flappyPanel extends JPanel
{
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FlappyBird.flappyBird.repaint(g);
    }
}
