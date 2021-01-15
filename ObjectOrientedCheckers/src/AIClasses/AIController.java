package AIClasses;

import Move.Move;
import Move.MoveCalcTree;
import Move.MoveToAction;
import ObjectUI.Main;
import Piece.BlackPiece;
import Piece.Piece;
import Piece.WhitePiece;
import TrueMinimax.TrueMinimaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Gamecontroller.*;
import MonteCarlo.mcTreeSearch;

public class AIController {
	
	public static Random rn = new Random();
	
	public static double[] weights = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
	public static double[] weightsAI1 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
	public static double[] weightsAI2 = {1.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0};
	
	public static boolean stochasticMinimax = true;
	
	public static Move getAiMove(Object[][] board, List<Move> MadeMoves) {
		if(Gamecontroller.turn%2 == 0) {
			if(Gamecontroller.AIone == 0) {
				Move aiMove = AIController.getRandomMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				return aiMove;
			}
			else if(Gamecontroller.AIone == 1) {
				mcTreeSearch tree = new mcTreeSearch(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				tree.setWithRAVE(true);
				Move move = tree.getNextMove();

				String GREEN = "\033[0;32m";
				String RESET = "\033[0m";  // Text Reset
				String PURPLE = "\033[0;35m";  // PURPLE
//
//				for(int i = 0; i<= Gamecontroller.field.length - 1; i++) {
//					Object[] arr = Gamecontroller.field[i];
//					for(Object obj : arr)
//					{
//						if(obj instanceof BlackPiece)
//							System.out.print(GREEN+"B  "+RESET);
//						else if(obj instanceof WhitePiece)
//							System.out.print(PURPLE+"W  "+RESET);
//						else if(obj == null)
//							System.out.print("0  ");
//					}
//					System.out.println();
//				}
//				System.out.println();
//				System.out.println();
//
//				System.out.println();
//				System.out.println("move from x= "+move.getPiece().getLocation()[0]+" y= "+move.getPiece().getLocation()[1]);
//				System.out.println("move to x= "+move.getToList().get(move.getToList().size()-1)[0]+" y= "+move.getToList().get(move.getToList().size()-1)[1]);
//				System.out.println("number of eaten pieces = "+move.getRemoveList().size());
				if(move.getRemoveList().size() == 2) {
					Piece ep1, ep2;
					ep1 = move.getRemoveList().get(0);
					ep2 = move.getRemoveList().get(1);
//					System.out.println("ep1 x = "+ep1.getLocation()[0]+" y= "+ep1.getLocation()[1]);
//					System.out.println("ep2 x = "+ep2.getLocation()[0]+" y= "+ep2.getLocation()[1]);
				}
//				System.out.println();

				return move;
			}
			else {
				AIController.weights = weightsAI1;
				TrueMinimaxTree.MAX_DEPTH = Gamecontroller.AIone - 1;
				Object[][] holdBoard = Gamecontroller.deepBoardCopy(board); // too early
				Move aiMove = TrueMinimaxTree.getMove(holdBoard, MadeMoves);
				return aiMove;
			}
		}
		else {
			if(Gamecontroller.AItwo == 0) {
				Move aiMove = AIController.getRandomMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				return aiMove;
			}
			else if(Gamecontroller.AItwo == 1) {
				mcTreeSearch tree = new mcTreeSearch(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				tree.setWithRAVE(true);
				Move move = tree.getNextMove();

				String GREEN = "\033[0;32m";
				String RESET = "\033[0m";  // Text Reset
				String PURPLE = "\033[0;35m";  // PURPLE

//				for(int i = 0; i<= Gamecontroller.field.length - 1; i++) {
//					Object[] arr = Gamecontroller.field[i];
//					for(Object obj : arr)
//					{
//						if(obj instanceof BlackPiece)
//							System.out.print(GREEN+"B  "+RESET);
//						else if(obj instanceof WhitePiece)
//							System.out.print(PURPLE+"W  "+RESET);
//						else if(obj == null)
//							System.out.print("0  ");
//					}
//					System.out.println();
//				}
//				System.out.println();
//				System.out.println();
//
//				System.out.println();
//				System.out.println("move from x= "+move.getPiece().getLocation()[0]+" y= "+move.getPiece().getLocation()[1]);
//				System.out.println("move to x= "+move.getToList().get(move.getToList().size()-1)[0]+" y= "+move.getToList().get(move.getToList().size()-1)[1]);
//				System.out.println("number of eaten pieces = "+move.getRemoveList().size());
				if(move.getRemoveList().size() == 2) {
					Piece ep1, ep2;
					ep1 = move.getRemoveList().get(0);
					ep2 = move.getRemoveList().get(1);
//					System.out.println("ep1 x = "+ep1.getLocation()[0]+" y= "+ep1.getLocation()[1]);
//					System.out.println("ep2 x = "+ep2.getLocation()[0]+" y= "+ep2.getLocation()[1]);
				}
//				System.out.println();

				return move;
				
			}
			else {
				AIController.weights = weightsAI2;
				TrueMinimaxTree.MAX_DEPTH = Gamecontroller.AItwo - 1;
				Object[][] holdBoard = Gamecontroller.deepBoardCopy(board); // too early
				Move aiMove = TrueMinimaxTree.getMove(holdBoard, MadeMoves);
				return aiMove;
			}
		}
	}
	
	public static Object[][] calcBlackMoves(Object[][] field, List<Move> MadeMoves){
		boolean captureMove = false;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 0) {
						((Piece) field[i][j]).AIcalcMoves(field);
						List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
						if(!captureMove) {
							for (int k = 0; k <= holdMoveList.size() - 1; k++) {
								if(holdMoveList.get(k).getRemoveList().size() != 0) {
									captureMove = true;
									break;
								}
							}
						}
					}
					else {
						((Piece) field[i][j]).resetMoveList();
					}
				}
			}		
		}
		if (captureMove) {
			for(int i = 0; i <= field.length - 1; i++) {
				for(int j = 0; j <= field[0].length - 1; j++) {
					if(field[i][j] != null) {
						if(((Piece) field[i][j]).getColour() == 0) {	
							List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
							for (int k = holdMoveList.size() - 1; k >= 0; k--) {
								if(holdMoveList.get(k).getRemoveList().size() == 0) {
									((Piece) field[i][j]).getMoves().remove(k);
								}
							}
						}
					}
				}
			}
		}
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
					for (int k = holdMoveList.size() - 1; k >= 0; k--) {
						if(Gamecontroller.illegalMove(holdMoveList.get(k), MadeMoves)) {
							((Piece) field[i][j]).getMoves().remove(k);
						}
					}
				}		
			}		
		}
		return field;
	}
	
	public static Object[][] calcWhiteMoves(Object[][] field, List<Move> MadeMoves){
		
		boolean captureMove = false;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 1) {		
						((Piece) field[i][j]).AIcalcMoves(field);
						List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
						if(!captureMove) {
							for (int k = 0; k <= holdMoveList.size() - 1; k++) {
								if(holdMoveList.get(k).getRemoveList().size() != 0) {
									captureMove = true;
									break;
								}
							}
						}
					}
					else {
						((Piece) field[i][j]).resetMoveList();
					}
				}
			}
		}
		if(captureMove) {
			for(int i = 0; i <= field.length - 1; i++) {
				for(int j = 0; j <= field[0].length - 1; j++) {
					if(field[i][j] != null) {
						if(((Piece) field[i][j]).getColour() == 1) {	
							List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
							for (int k = holdMoveList.size() - 1; k >= 0; k--) {
								if(holdMoveList.get(k).getRemoveList().size() == 0) {
									((Piece) field[i][j]).getMoves().remove(k);
								}
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
					for (int k = holdMoveList.size() - 1; k >= 0; k--) {
						if(Gamecontroller.illegalMove(holdMoveList.get(k), MadeMoves)) {
							((Piece) field[i][j]).getMoves().remove(k);
						}
					}
				}		
			}		
		}
		return field;
	}
	
	public static void testBoard(Object[][] field) {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] == null) {
					System.out.print("--");
				}
				else {
					System.out.print(((Piece)field[i][j]).getMoves().size() + "" + ((Piece)field[i][j]).getColour());
				}
			}
			System.out.println();
		}
		System.out.println("-------------");
	}
	
	public static double boardEval(Object[][] board, int colour, boolean endGame) {
		int movecount = 0;
		int amountFriendlyPieces = 0;
		int amountEnemyPieces = 0;
		int amountFriendlyKings = 0;
		int amountEnemyKings = 0;
		
		int normalmoves = 0;
		int becomesKingmove = 0;
		int capturemoves = 0;
		
		List<int[]> enemyPos = new ArrayList();
		List<int[]> friendlyPos = new ArrayList();
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if(board[i][j] != null) {
					movecount += ((Piece)board[i][j]).getMoves().size();
					if(((Piece)board[i][j]).getColour() == colour) {
						List<Move> holdlist = ((Piece)board[i][j]).getMoves();
						for(int k = 0; k <= holdlist.size() - 1; k++) {
							if (holdlist.get(k).becomesKing()) {
								becomesKingmove++;
							}
							if (holdlist.get(k).getRemoveList().size() > 0) {
								capturemoves++;
							}
							if(!holdlist.get(k).becomesKing() && holdlist.get(k).getRemoveList().size() == 0) {
								normalmoves++;
							}
						}
						friendlyPos.add(((Piece)board[i][j]).getLocation());
						if(((Piece)board[i][j]).isKing()) {
							amountFriendlyKings++;
						}
						else {
							amountFriendlyPieces++;
						}
					}
					else {
						enemyPos.add(((Piece)board[i][j]).getLocation());
						if(((Piece)board[i][j]).isKing()) {
							amountEnemyKings++;
						}
						else {
							amountEnemyPieces++;
						}
					}
				}
			}
		}
		if(movecount > 0) {
			double stochvalue = 0.0;
			
			if(stochasticMinimax)
				stochvalue = rn.nextDouble()/10.0;
			
			double newValue = weights[4]*(double)normalmoves + weights[5]*becomesKingmove + weights[6]*capturemoves;
			return ((double)((weights[0]*(double)amountFriendlyPieces + weights[1]*(double)amountFriendlyKings) - (weights[2]*(double)amountEnemyPieces + weights[3]*(double)amountEnemyKings)) + newValue + stochvalue);
		}
		else {
			if((amountFriendlyKings + amountFriendlyPieces) > 0 && (amountEnemyKings + amountEnemyPieces) == 0) {
				return 1000.0;
			}
			if((amountFriendlyKings + amountFriendlyPieces) == 0 && (amountEnemyKings + amountEnemyPieces) > 0) {
				return -1000.0;
			}
			return -0.5;
		}
		
	}
	
	public static Move getRandomMove(Object[][] board) {
		List<Move> movelist = new ArrayList();
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if(board[i][j] != null) {
					movelist.addAll(((Piece)board[i][j]).getMoves());
				}
			}
		}
		return movelist.get(rn.nextInt(movelist.size()));
	}

}
