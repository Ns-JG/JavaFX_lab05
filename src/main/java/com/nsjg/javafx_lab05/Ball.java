package com.nsjg.javafx_lab05;

import javafx.scene.shape.Rectangle;

public class Ball extends Thread {
    private final Double origin_start;
    public volatile int[] position = {0, 0};
    public int direction;
    public static int size_y = 100;
    public static int size_x = 100;
    public volatile Double x_translation = 0.0;
    protected int activation_time = getRandomNumber(300);
    private int immune = 0;
    public volatile boolean lock = false;
    public Ball(int start_x, int start_y, int size_y, int size_x) {
        this.origin_start =  (double) start_x;
        position[0] = start_x;
        position[1] = start_y;
        Ball.size_y = size_y;
        Ball.size_x = size_x;
        if ( getRandomNumber(10) > 5 ) { direction = 1; }
        else { direction = -1; }
    }
    public void start() { super.start(); }
    public void run() {
        while (activation_time > 0) { try { sleep(50); } catch (InterruptedException e) { throw new RuntimeException(e); } activation_time--; }
        while (true) {
            if ( this.lock ) { continue; }
            immune--;
            try { sleep(5); } catch (InterruptedException e) { throw new RuntimeException(e); }
            if (position[0] < 0) { direction = 1; }
            if (position[0] + size_x > 1000) { direction = -1; }
            x_translation = this.position[0] - origin_start;
            move(direction);
        }
    }
    public synchronized void check_collision( Rectangle player_rect ) {
        if ( player_rect.intersects(this.position[0], this.position[1], Ball.size_x, Ball.size_y) && this.immune <= 0 )
        {
            this.direction = -1*direction;
            this.immune = 300;
        }
    }
    private int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)) + 0);
    }
    private void move(int direction) { this.position[0] += (direction); }
}