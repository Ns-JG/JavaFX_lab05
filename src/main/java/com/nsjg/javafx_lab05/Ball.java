package com.nsjg.javafx_lab05;

import javafx.scene.shape.Rectangle;

import java.util.Arrays;

public class Ball extends Thread {

    public volatile int[] position = {0, 0};
    //public Rectangle rect = new Rectangle(position[0], position[1], 100, 100);
    private int direction = 1;

    public Ball(int start_x, int start_y) {
        position[0] = start_x;
        position[1] = start_y;
    }

    public void start() { super.start(); }

    public void run() {

        while( true ) {
            System.out.println("poz:\t"+ Arrays.toString(position));
            try {
                sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (position[1] < 0) { direction=1; }
            if (position[1] + 100 > 1000) { direction=-1; }
            move(direction);
        }

    }
    public void move(int direction) { this.position[1] += 4*(direction%2); }
}
