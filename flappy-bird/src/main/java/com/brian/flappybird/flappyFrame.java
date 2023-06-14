package com.brian.flappybird;

import javax.swing.JFrame;

public class flappyFrame extends JFrame{



    public flappyFrame(){

       add(new Renderer());
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String[] args){
        new flappyFrame();
    }
}
