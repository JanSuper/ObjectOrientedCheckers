package ObjectUI;

import Gamecontroller.Gamecontroller;
import MonteCarlo.mcTreeSearch;
import Move.Move;
import Move.MoveToAction;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
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

        stage.setTitle("Checkers game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if(Gamecontroller.playerOneAI) {
            //do first AI move
            mcTreeSearch tree = new mcTreeSearch(Gamecontroller.deepBoardCopy(Gamecontroller.field));
            tree.setOGturn(0);
            Move move = tree.getNextMove();
            MoveToAction.AIAction(move);
        }
        launch(args);
    }
}