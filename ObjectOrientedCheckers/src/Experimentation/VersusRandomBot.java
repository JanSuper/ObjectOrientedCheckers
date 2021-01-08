package Experimentation;

import Gamecontroller.Gamecontroller;

public class VersusRandomBot {
	
	public static void main(String[] args){
		Gamecontroller game = new Gamecontroller();
		game.Visuals = false;
		
		for(int i = 1; i <= 11; i++) {
			int wins = 0;
			int draws = 0;
			
			double[] P1 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
			double[] P2 = {};
		
			int P1AI = i + 1;
			int P2AI = 0;
			
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
				
			}
			System.out.println("Depth " + i + ": " + wins + "% - " + draws + "%");
		}
	}

}
