package Gamecontroller;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Move.Move;
import Move.MoveToAction;
import Piece.Piece;
import UI.BoardPanel;

public class Gamecontroller {
	public static Board board;
	public static Object[][] field;
	public static int turn = 0;
	
	public static void main(String[] args) {
		newGame();
		//testBoard();
		calcBlackMoves();
		testPieces();	
		Move firstMove = new Move(((Piece)field[2][1]));
		firstMove.setTo(3, 2);
		MoveToAction.UserAction(firstMove);	
		testPieces();	
		firstMove = new Move(((Piece)field[5][4]));
		firstMove.setTo(4, 3);
		MoveToAction.UserAction(firstMove);
		testPieces();
		firstMove = new Move(((Piece)field[3][2]));
		firstMove.setTo(5, 4);
		MoveToAction.UserAction(firstMove);
		testPieces();
		endTurn();
		testPieces();
		firstMove = new Move(((Piece)field[2][3]));
		firstMove.setTo(3, 2);
		MoveToAction.UserAction(firstMove);
		testPieces();
//		firstMove = new Move(((Piece)field[6][5]));
//		firstMove.setTo(4, 3);
//		MoveToAction.UserAction(firstMove);
//		testPieces();
//		firstMove = new Move(((Piece)field[4][3]));
//		firstMove.setTo(2, 1);
//		MoveToAction.UserAction(firstMove);
		
		MoveToAction.AIAction(((Piece)field[6][5]).getMoves().get(0));
		
		testPieces();
	}
	
	public static void newGame() {
		board = new Board();
		field = board.getBoard();
		turn = 0;
	}
	
	public static void calcBlackMoves() {
		boolean captureMove = false;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 0) {
						((Piece) field[i][j]).calcMoves();
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
	}
	
	public static void endTurn() {
		turn++;
		if(turn%2==0) {
			calcBlackMoves();
			System.out.println("Black's turn");
		}
		else {
			calcWhiteMoves();
			System.out.println("White's turn");
		}
		
		boolean endOfGame = true;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getMoves().size() != 0) {
						endOfGame = false;
						break;
					}
				}
			}
			if (!endOfGame) {
				break;
			}
		}
		
		if(endOfGame) {
			System.out.println("end of game");
		}
		
	}
	
	public static void calcWhiteMoves() {
		boolean captureMove = false;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 1) {		
						((Piece) field[i][j]).calcMoves();
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
	}
	
	public static void testBoard() {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] == null) {
					System.out.print("0");
				}
				else {
					System.out.print("1");
				}
			}
			System.out.println();
		}
	}
	
	public static void testPieces() {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] == null) {
					System.out.print("-");
				}
				else {
					System.out.print(((Piece)field[i][j]).getMoves().size());
				}
			}
			System.out.println();
		}
		System.out.println("-------------");
	}
	
	public static void limitToThis(int icoor, int jcoor) {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if (field[i][j] != null) {
					if (i != icoor || j != jcoor) {
						((Piece)field[i][j]).resetMoveList();
					}
				}
			}
		}
	}

}
