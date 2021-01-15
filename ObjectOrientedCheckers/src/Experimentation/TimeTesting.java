package Experimentation;

import AIClasses.AIController;
import Gamecontroller.Gamecontroller;
import TrueMinimax.TrueMinimaxTree;

public class TimeTesting {
	
	public static void main(String[] args) {
		Gamecontroller game = new Gamecontroller();
		game.Visuals = false;
		
		AIController.stochasticMinimax = false;
		
		for(int i = 1; i <= 11; i++) {
			int wins = 0;
			int draws = 0;
			
			double[] P1 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
			double[] P2 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
			
			int P1AI = i + 1;
			int P2AI = 0;
			
			long time = System.currentTimeMillis();
			
			TrueMinimaxTree.pruning = true;
			
			int nodesPrun = 0;
			for(int j = 0; j <= 99; j++) {
				if( j < 50) {
					game = new Gamecontroller();

					game.gameLoop(P1, P2, P1AI, P2AI);
			
			
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						draws++;
					}
					else if (game.playerOneWon) {
						wins++;
					}
				}
				else {
					game = new Gamecontroller();

					game.gameLoop(P2, P1, P2AI, P1AI);
			
			
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						draws++;
					}
					else if (!game.playerOneWon) {
						wins++;
					}
				}
				nodesPrun += TrueMinimaxTree.nodes;
				
			}
			System.out.println("Depth " + i + ": " + wins + "% - " + draws + "%");
			
			wins = 0;
			draws = 0;
			
			long timeAfterfirst = System.currentTimeMillis() - time;
			time = System.currentTimeMillis();
			
			TrueMinimaxTree.pruning = false;
			int nodesWithoutPrun = 0;
			for(int j = 0; j <= 99; j++) {
				if( j < 50) {
					game = new Gamecontroller();

					game.gameLoop(P1, P2, P1AI, P2AI);
			
			
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						draws++;
					}
					else if (game.playerOneWon) {
						wins++;
					}
				}
				else {
					game = new Gamecontroller();

					game.gameLoop(P2, P1, P2AI, P1AI);
			
			
					if(game.turn == game.MAX_TURNS || game.gameDraw) {
						draws++;
					}
					else if (!game.playerOneWon) {
						wins++;
					}
				}
				nodesWithoutPrun += TrueMinimaxTree.nodes;
			}
			System.out.println("Depth " + i + ": " + wins + "% - " + draws + "%");
			
			long timeAftersecond = System.currentTimeMillis() - time;
			
			System.out.println(nodesPrun + " vs " + nodesWithoutPrun);
			
			System.out.println("Depth " + i + " Alpa-Beta is " + (double)nodesWithoutPrun/(double)nodesPrun + " times more effecient");
		}
	}

}
