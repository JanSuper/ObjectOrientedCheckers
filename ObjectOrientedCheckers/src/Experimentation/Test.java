package Experimentation;

import Gamecontroller.Gamecontroller;

public class Test {
	
	public static void main(String[] args) {
		Gamecontroller game = new Gamecontroller();
		game.Visuals = false;
		
		game.gameLoop();
		
		
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
