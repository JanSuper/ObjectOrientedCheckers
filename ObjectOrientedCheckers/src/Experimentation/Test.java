package Experimentation;

import Gamecontroller.Gamecontroller;

public class Test {
	
	public static void main(String[] args) {
		Gamecontroller game = new Gamecontroller();
		game.Visuals = false;
		
		double[] P1 = {1.0, 2.0, 1.0, 2.0};
		double[] P2 = {1.0, 2.0, 1.0, 2.0};
		
		game.gameLoop(P1, P2);
		
		
		if(game.turn == game.MAX_TURNS) {
			System.out.println("draw");
		}
		else if (game.playerOneWon) {
			System.out.println("Player one wins");
		}
		else {
			System.out.println("player two wins");
		}
		
		game = new Gamecontroller();
		game.Visuals = false;
		
		game.gameLoop(P1, P2);
		
		
		if(game.turn == game.MAX_TURNS) {
			System.out.println("draw");
		}
		else if (game.playerOneWon) {
			System.out.println("Player one wins");
		}
		else {
			System.out.println("player two wins");
		}
	}

}
