package com.nsjg.javafx_lab05;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Game game = new Game(root, 1000,1000, 12, 12, 2);
        stage.setScene(game);
        stage.setOnCloseRequest(windowEvent -> { Platform.exit(); System.exit(0); });
        stage.setTitle("GAME");
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}