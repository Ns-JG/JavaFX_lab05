package com.nsjg.javafx_lab05;

import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

public class Player extends Thread {
    private final Double origin_start;
    public volatile int[] position = {0, 0};
    private int direction;
    public static int size_y = 100;
    public static int size_x = 100;
    public volatile Double y_translation = 0.0;
    private final Game game;
    public Player(Game game, int start_x, int start_y, int size_y, int size_x) {
        this.origin_start =  (double) start_y;
        position[0] = start_x;
        position[1] = start_y;
        Player.size_y = size_y;
        Player.size_x = size_x;
        this.game = game;
        if ( getRandomNumber(10) > 5 ) { direction = 1; }
        else { direction = -1; }
    }
    public void start() { super.start(); }
    public void run() {
        while ( true ) {
            try { sleep(10);} catch (InterruptedException e) { throw new RuntimeException(e); }
            game.balls.forEach( ball -> ball.check_collision( new Rectangle(this.position[0], this.position[1], Player.size_x, Player.size_y) ));
            var ball_y = 500;
            var comp_1 = new ArrayList<Integer>();
            var comp_2 = new ArrayList<Integer>();
            game.balls.forEach(ball -> { comp_1.add(ball.position[0]); comp_2.add(ball.position[0]*ball.direction); } );
            if ( this.position[0] < 500 ) { ball_y = game.balls.get(comp_1.indexOf(Collections.min(comp_1))).position[1]; }
            if ( this.position[0] > 500 ) { ball_y = game.balls.get(comp_2.indexOf(Collections.max(comp_2))).position[1]; }
            this.direction = Integer.compare(ball_y, this.position[1]);
            move(direction);
            y_translation = this.position[1] - origin_start;
        }
    }
    private void move(int direction) { this.position[1] += 4*direction; }
    private int getRandomNumber(int max) { return (int) ((Math.random() * (max))); }
}