package AIClasses;

import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;
import SimpleAI.SearchTree;
import TrueMinimax.TrueMinimaxTree;

import java.util.ArrayList;
import java.util.List;

import Gamecontroller.*;

public class AIController {
	
	public static Move getAiMove(Object[][] board) {
		Object[][] holdBoard = Gamecontroller.deepBoardCopy(board); // too early
		
		Move move = TrueMinimaxTree.getMove(holdBoard);
		//this class do be kinda useless tho ngl
		
		return move;
	}
	
	public static Object[][] calcBlackMoves(Object[][] field){
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
		return field;
	}
	
	public static Object[][] calcWhiteMoves(Object[][] field){
		
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
	
	public static int[] moveScore (Object[][] board, Move move) {
		
		int amountTaken = move.getRemoveList().size();
		
		board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
		
		if (move.getPiece().getColour() == 1) {
			board = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(board));
		}
		else {
			board = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(board));
		}
		
		int[] currentPos = move.getPiece().getLocation();
		int amountFriendlyPieces = 0;
		int amountEnemyPieces = 0;
		int amountFriendlyKings = 0;
		int amountEnemyKings = 0;
			for(int i = 0; i <= 7; i++) {
				for(int j = 0; j <= 7; j++) {
					if(board[i][j] != null) {
						if(((Piece)board[i][j]).getColour() == move.getPiece().getColour()) {
							if(((Piece)board[i][j]).isKing()){
								amountFriendlyKings++;
							}
							else {
								amountFriendlyPieces++;
							}
						}
						else {
							if(((Piece)board[i][j]).isKing()){
								amountEnemyKings++;
							}
							else {
								amountEnemyPieces++;
							}
						}
					}
				}
			}
		
		int[] score = {amountFriendlyPieces, amountFriendlyKings, amountEnemyPieces, amountEnemyKings};
		return score;
	}
	
	public static double boardEval(Object[][] board, int colour) {
		int movecount = 0;
		int amountFriendlyPieces = 0;
		int amountEnemyPieces = 0;
		int amountFriendlyKings = 0;
		int amountEnemyKings = 0;
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if(board[i][j] != null) {
					movecount += ((Piece)board[i][j]).getMoves().size();
					if(((Piece)board[i][j]).getColour() == colour) {
						if(((Piece)board[i][j]).isKing()) {
							amountFriendlyKings++;
						}
						else {
							amountFriendlyPieces++;
						}
					}
					else {
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
			return ((double)((amountFriendlyPieces + 2*amountFriendlyKings) - (amountEnemyPieces + 2*amountEnemyKings)));
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
