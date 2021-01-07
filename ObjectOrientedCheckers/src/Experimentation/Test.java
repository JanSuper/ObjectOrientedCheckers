package Experimentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import Gamecontroller.Gamecontroller;

public class Test {
	
	public static Random rn = new Random();
	
	public static void main(String[] args) {
		Gamecontroller game = new Gamecontroller();
		game.Visuals = false;

		 try {
		      File myObj = new File("filename.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		      BufferedWriter writer = new BufferedWriter(new FileWriter("filename.txt"));
		      
		      for(int i = 0; i <= 99; i++) {
		    	  
		    	  System.out.println(i);
		    	  
		    	  game = new Gamecontroller();
		      	
		    	  int gameOne = 0;
		      		int gameTwo = 0;
				
					double[] P1 = {rn.nextDouble()*4.0-2.0, rn.nextDouble()*4.0-2.0, rn.nextDouble()*4.0-2.0, rn.nextDouble()*4.0-2.0};
					double[] P2 = {1.0, 2.0, 1.0, 2.0};
				
					int P1depth = 5;
					int P2depth = 5;
				
					game.gameLoop(P1, P2, P1depth, P2depth);
				
				
					if(game.turn == game.MAX_TURNS) {
//						System.out.println("draw");
					}
					else if (game.playerOneWon) {
//						System.out.println("Player one wins");
						gameOne = 1;
					}
					else {
//						System.out.println("player two wins");
					}
				
					game = new Gamecontroller();
					game.Visuals = false;
				
					game.gameLoop(P2, P1, P2depth, P1depth);
				
				
					if(game.turn == game.MAX_TURNS) {
//						System.out.println("draw");
					}
					else if (game.playerOneWon) {
//						System.out.println("Player one wins");
					}
					else {
//						System.out.println("player two wins");
						gameTwo = 1;
					}
		      
				writer.write(Arrays.toString(P1) + " " + gameOne + " " + gameTwo);
		      	writer.newLine();
		      
		      }
		      writer.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

}
