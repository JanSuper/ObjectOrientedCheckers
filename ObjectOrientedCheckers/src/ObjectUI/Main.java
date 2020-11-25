package ObjectUI;

import Board.Board;
import Gamecontroller.Gamecontroller;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {
    public static Visual board = new Visual();
    public SmartStage stage = new SmartStage(board);

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(board, WIDTH, HEIGHT, true);
        scene.setCamera(camera);

        stage.setTitle("title");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
    	if(Gamecontroller.playerOneAI) {
       	 //do first AI move
        }
        launch(args);
    }

}