package AIClasses;

import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;
import TrueMinimax.TrueMinimaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Gamecontroller.*;

public class AIController {
	
	public static Random rn = new Random();
	
	public static Move getAiMove(Object[][] board, List<Move> MadeMoves) {
		Object[][] holdBoard = Gamecontroller.deepBoardCopy(board); // too early
		
		Move move = TrueMinimaxTree.getMove(holdBoard, MadeMoves);
		//this class do be kinda useless tho ngl
		
		return move;
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
		List<int[]> enemyPos = new ArrayList();
		List<int[]> friendlyPos = new ArrayList();
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if(board[i][j] != null) {
					movecount += ((Piece)board[i][j]).getMoves().size();
					if(((Piece)board[i][j]).getColour() == colour) {
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
			if(true) {
				return ((double)((amountFriendlyPieces + 2*amountFriendlyKings) - (amountEnemyPieces + 2*amountEnemyKings)));
			}
			else {
				//calculate farthest Manhattan distances
				double distance = 0.0;
				for(int i = 0; i <= friendlyPos.size() - 1; i++) {
					double holdDistance = 0.0;
					int[] fPos = friendlyPos.get(i);
					for(int j = 0; j <= enemyPos.size() - 1; j++) {
						int[] ePos = enemyPos.get(j);
						if(((double)Math.abs(fPos[0] - ePos[0]) + Math.abs(fPos[1] - ePos[1])) > holdDistance) {
							holdDistance = ((double)Math.abs(fPos[0] - ePos[0]) + Math.abs(fPos[1] - ePos[1]));
						}
					}
					distance += holdDistance;
				}
				double maxDistance = (double)(friendlyPos.size() * 16 + 1);
				
				return ((double)((amountFriendlyPieces + 2*amountFriendlyKings) - (amountEnemyPieces + 2*amountEnemyKings)) + distance/maxDistance);
			}
		}
		else {
			if((amountFriendlyKings + amountFriendlyPieces) > 0 && (amountEnemyKings + amountEnemyPieces) == 0) {
				return 100.0;
			}
			if((amountFriendlyKings + amountFriendlyPieces) == 0 && (amountEnemyKings + amountEnemyPieces) > 0) {
				return -100.0;
			}
			return -0.5;
		}
		
	}

}
