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
		      	
		    	  	double gameOne = 0.0;
		      		double gameTwo = 0.0;
				
					double[] P1 = {rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0};
					double[] P2 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
				
					int P1AI = 8;
					int P2AI = 4;
				
					game.gameLoop(P1, P2, P1AI, P2AI);
				
				
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						System.out.println("draw");
					}
					else if (game.playerOneWon) {
//						System.out.println("Player one wins");
						gameOne = 1.0;
					}
					else {
//						System.out.println("player two wins");
					}
				
					game = new Gamecontroller();
					game.Visuals = false;
					
					double[] P1new = {rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0, rn.nextDouble()*2.0};
				
					game.gameLoop(P2, P1new, P2AI, P1AI);
				
				
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						System.out.println("draw");
					}
					else if (game.playerOneWon) {
//						System.out.println("Player one wins");
					}
					else {
//						System.out.println("player two wins");
						gameTwo = 1.0;
					}
					
					//TODO change way of noting wins
					String weights = "";
					for(int j = 0; j <= P1.length - 1; j++) {
						weights += (String)(P1[j] + " ");
					}
					
					String weightsnew = "";
					for(int j = 0; j <= P1new.length - 1; j++) {
						weightsnew += (String)(P1new[j] + " ");
					}
		      
				writer.write(weights + " " + gameOne);
		      	writer.newLine();
		      	writer.write(weightsnew + " " + gameTwo);
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
