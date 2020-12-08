package AIClasses;

import MonteCarlo.mcTreeSearch;
import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;
import SimpleAI.SearchTree;

import java.util.ArrayList;
import java.util.List;

import Gamecontroller.*;

public class AIController {
	
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
							for (int k = 0; k <= holdMoveList.size() - 1; k++) {
								if(holdMoveList.get(k).getRemoveList().size() == 0) {
									((Piece) field[i][j]).resetMoveList();
									break;
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
							for (int k = 0; k <= holdMoveList.size() - 1; k++) {
								if(holdMoveList.get(k).getRemoveList().size() == 0) {
									((Piece) field[i][j]).resetMoveList();
									break;
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
		// Amount taken | Amount to be lost | new King |  Total friendly in own half | Total enemy in own half
		
		int amountTaken = move.getRemoveList().size();
		
		board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
		
		if (move.getPiece().getColour() == 1) {
			board = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(board));
		}
		else {
			board = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(board));
		}
		
		int amountToBeTaken = 0;
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if (board[i][j] != null) {
					List<Move> possibleMoves = ((Piece)board[i][j]).getMoves();
					for(int k = 0; k <= possibleMoves.size() - 1; k++) {
						if (amountToBeTaken < possibleMoves.get(k).getRemoveList().size()) {
							amountToBeTaken = possibleMoves.get(k).getRemoveList().size();
						}
					}
				}
			}
		}
		int king = -1;
		if(move.becomesKing()) {
			king = 1;
		}
		else {
			king = 0;
		}
		
		int[] currentPos = move.getPiece().getLocation();
		int amountFriendly = 0;
		int amountEnemy = 0;
		if (currentPos[0] <= 3) {
			for(int i = 0; i <= 3; i++) {
				for(int j = 0; j <= 7; j++) {
					if(board[i][j] != null) {
						if(((Piece)board[i][j]).getColour() == move.getPiece().getColour()) {
							amountFriendly++;
						}
						else {
							amountEnemy++;
						}
					}
				}
			}
		}
		else {
			for(int i = 4; i <= 7; i++) {
				for(int j = 0; j <= 7; j++) {
					if(board[i][j] != null) {
						if(((Piece)board[i][j]).getColour() == move.getPiece().getColour()) {
							amountFriendly++;
						}
						else {
							amountEnemy++;
						}
					}
				}
			}
		}
		
		int[] score = {amountTaken, amountToBeTaken, king, amountFriendly, amountEnemy};
		return score;
	}

}
