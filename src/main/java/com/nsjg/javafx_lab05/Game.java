package com.nsjg.javafx_lab05;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class Game extends Scene {
    private final Group root;
    public volatile ArrayList<Player> players = new ArrayList<>();
    public volatile ArrayList<Ball> balls = new ArrayList<>();
    public volatile ArrayList<ArrayList<Rectangle>> synchronized_fields = new ArrayList<>();
    private final ArrayList<Integer> available_staring_points;
    public Game(Group root, int width, int height, int rows, int columns, int starting_balls) throws IOException{
        super(root, width, height);
        super.setFill(Color.BLACK);
        this.root = root;
        if( rows == 0 ) { rows = 10; }
        if( columns == 0 ) { columns = 10; }
        //if (starting_balls > rows) { System.out.println("not possible"); throw customEXP }
        int row_offset = height / rows;
        int col_offset = width / columns;

        Image ball_image = new Image(Objects.requireNonNull(Game.class.getResource("kappa.png")).openStream());
        Image left_player_image = new Image(Objects.requireNonNull(Game.class.getResource("player_left.jpg")).openStream());
        Image right_player_image = new Image(Objects.requireNonNull(Game.class.getResource("player_right.jpg")).openStream());

        for(int row = 0; row < rows; row++ ) {
            var new_row = new ArrayList<Rectangle>();
            new_row.add(new Rectangle(0, row_offset *row, col_offset, row_offset)); // right side
            new_row.add(new Rectangle(1000- row_offset, row_offset *row, col_offset, row_offset)); //left side
            synchronized_fields.add(new_row); // point fields
        }

        available_staring_points = new ArrayList<>();
        IntStream stream = IntStream.range(0, columns);
        stream.forEach( n -> available_staring_points.add(0) );
        var balls_to_spawn = starting_balls;
        while (balls_to_spawn > 0) {
            var choice = getRandomNumber(columns);
            if (available_staring_points.get(choice) == 0) {
                available_staring_points.set(choice, 1);
                balls_to_spawn--;
            }
        }

        for (int i = 0; i < available_staring_points.size(); i++) {
            var hide = false; var lock = false;
            if ( available_staring_points.get(i) == 0) { hide = true; lock = true; }
            Ball.size_x = col_offset; Ball.size_y = row_offset;
            var new_ball = new Ball(500- col_offset /2, i* row_offset, row_offset, col_offset);
            if ( lock ) { new_ball.lock = true; }
            balls.add(new_ball);
            ImageView temp_ball = new ImageView(ball_image);
            temp_ball.setX(500- col_offset /2.0); temp_ball.setY(i* row_offset);
            temp_ball.setFitWidth(Ball.size_x); temp_ball.setFitHeight(Ball.size_y); root.getChildren().add(temp_ball);
            if ( hide ) { temp_ball.setVisible(false); }

        }

        players.add(new Player(this, col_offset /2, 500- row_offset, row_offset, col_offset /2));
        var left_player = new ImageView(left_player_image); left_player.setX(col_offset /2.0); left_player.setY(500- row_offset); left_player.setFitHeight(row_offset); left_player.setFitWidth(col_offset /2.0);
        root.getChildren().add(left_player);

        players.add(new Player(this, 1000- col_offset,500- row_offset, row_offset, col_offset /2));
        var right_player = new ImageView(right_player_image); right_player.setX(1000- col_offset); right_player.setY(500- row_offset); right_player.setFitHeight(row_offset); right_player.setFitWidth(col_offset /2.0);
        root.getChildren().add(right_player);
        players.forEach(Player::start);

        balls.forEach(Ball::start);
        run();
    }

    public void run() {
        Task<Void> game_loop = new Task<>() {
            @Override
            public Void call() {
                while ( true ) {
                    check_points_fields();
                    for (int i = 0; i < root.getChildren().size()-2; i++ ) {
                        root.getChildren().get(i).setTranslateX(balls.get(i).x_translation);
                    }
                    root.getChildren().get(root.getChildren().size()-2).setTranslateY(players.get(0).y_translation);
                    root.getChildren().get(root.getChildren().size()-1).setTranslateY(players.get(1).y_translation);
                    try { Thread.sleep(60); } catch (InterruptedException e) { throw new RuntimeException(e); }
                }
            }
        };
        System.out.println("starting thread loop ");
        Thread loop = new Thread(game_loop);
        loop.setDaemon(true);
        loop.start();
    }
    private void check_points_fields() {
        for (Ball ball : balls) {
            if (ball.position[0] < 10 || ball.position[0] + Ball.size_x > 990) {
                synchronized (this) {
                    ball.position[0] = 500 - Ball.size_x / 2;
                    while (true) {
                        if ( !available_staring_points.contains(0) ) { return; } // overflow
                        var guess = getRandomNumber(available_staring_points.size());
                        if (available_staring_points.get(guess) == 0) {
                            available_staring_points.set(guess, 1);
                            balls.get(guess).lock = false;
                            root.getChildren().get(guess).setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    private int getRandomNumber(int max) { return (int) ((Math.random() * (max)) + 0); }
}
