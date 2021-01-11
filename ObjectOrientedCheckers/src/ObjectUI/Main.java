package ObjectUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

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
    	setGameRules();
        launch(args);
    }
    
    public static void setGameRules() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	String line = "";

    	   while (line.equalsIgnoreCase("true") == false && line.equalsIgnoreCase("false") == false) {
    		   System.out.println("Is P1 an AI?");
    	       try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	       //do something
    	   }
    	   
    	   Gamecontroller.playerOneAI = line.equalsIgnoreCase("true");
    	   
    	   if( Gamecontroller.playerOneAI) {
    		   int AITypeP1 = 0;
    		   line = "";
    		   boolean answer = false;
        	   while (!answer) {
        		   answer = true;
        		   System.out.println("What AI is P1");
        	       try {
    				line = in.readLine();
    				try {
    				AITypeP1 = Integer.parseInt(line);
    				} catch(NumberFormatException e) {
        				answer = false;
        			}
    				if (answer) {
    				answer = (AITypeP1 >= 0);
    				}
    			} catch (IOException e) {
    				answer = false;
    			}
        	  }
        	   Gamecontroller.AIone = AITypeP1;
    	   }
    	   
    	   
    	   
    	   line = "";

    	   while (line.equalsIgnoreCase("true") == false && line.equalsIgnoreCase("false") == false) {
    		   System.out.println("Is P2 an AI?");
    	       try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	       //do something
    	   }
    	   
    	   Gamecontroller.playerTwoAI = line.equalsIgnoreCase("true");
    	   
    	   
    	   if( Gamecontroller.playerTwoAI) {
    		   int AITypeP2 = 0;
    		   line = "";
    		   boolean answer = false;
        	   while (!answer) {
        		   answer = true;
        		   System.out.println("What AI is P2");
        	       try {
    				line = in.readLine();
    				try {
    				AITypeP2 = Integer.parseInt(line);
    				} catch(NumberFormatException e) {
        				answer = false;
        			}
    				if (answer) {
    				answer = (AITypeP2 >= 0);
    				}
    			} catch (IOException e) {
    				answer = false;
    			}
        	  }
        	   Gamecontroller.AItwo = AITypeP2;
    	   }
    }

}