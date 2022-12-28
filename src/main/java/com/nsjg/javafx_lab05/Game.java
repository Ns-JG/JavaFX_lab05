package com.nsjg.javafx_lab05;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Game extends Canvas {

    public GraphicsContext gc = this.getGraphicsContext2D();
    public ArrayList<Player> players = new ArrayList();
    public ArrayList<Ball> balls = new ArrayList<>();
    private Image ball_image;
    private Image player_image;
    private Ball temp;

    public Game(int width, int height, int rows, int columns, int starting_players, int starting_balls) throws IOException{
        super(width, height);
        assert rows == 0: rows = 10;
        assert columns == 0: columns = 20; // synchronizowane będą tylko kwadraty z kolizujących linii
        assert starting_balls == 0: starting_balls = 1;
        assert starting_players == 0: starting_players = 2; // right left side players ??
        ball_image = new Image(Objects.requireNonNull(Game.class.getResource("kappa.png")).openStream());
        setVisible(true);

        temp = new Ball(0,0);
        temp.start();
        run();
    }

    public void run() {
        Thread run_task = new Thread(() -> { while ( true ) {
//            System.out.println(temp.x+"\t "+temp.y);
            gc.clearRect(0,0,1000,1000);
            gc.drawImage(ball_image, temp.position[0], temp.position[1]);
            System.out.println("running canvas loop");
            try { Thread.sleep(20); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }
        });
        run_task.start();
    }
}
