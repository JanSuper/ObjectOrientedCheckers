package ObjectUI;

import Gamecontroller.Gamecontroller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mcVisualizerMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Gamecontroller.playerOneAI = true;
        Gamecontroller.playerTwoAI = false;

        mcVisualizerGroup group = new mcVisualizerGroup();

        Scene scene = new Scene(group, 1000, 800);

        mcVisualizerStage stage = new mcVisualizerStage(group);

        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) { 
    	//setGameRules();
        //launch(args);
		Application.launch(args);
    }
}
