package Gamecontroller;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Move.Move;
import Move.MoveToAction;
import ObjectUI.Main;
import ObjectUI.Visual;
import Piece.BlackPiece;
import Piece.CheckersPiece;
import Piece.Piece;
import Piece.PlaceholderPiece;
import Piece.WhitePiece;

public class Gamecontroller {
	public static Board board;
	public static Object[][] field;
	public static int turn = 0;
	public static boolean playerOneAI = false;
	public static boolean playerTwoAI = true;
	public static boolean gameOver = false;
	
	public Gamecontroller() {
		newGame();
		calcBlackMoves();
	}
	
//	public static void main(String[] args) {
//		newGame();
//		//testBoard();
//		calcBlackMoves();
//		testPieces();	
//		Move firstMove = new Move(((Piece)field[2][1]));
//		firstMove.setTo(3, 2);
//		MoveToAction.UserAction(firstMove);	
//		testPieces();	
//		firstMove = new Move(((Piece)field[5][4]));
//		firstMove.setTo(4, 3);
//		MoveToAction.UserAction(firstMove);
//		testPieces();
//		firstMove = new Move(((Piece)field[3][2]));
//		firstMove.setTo(5, 4);
//		MoveToAction.UserAction(firstMove);
//		testPieces();
//		endTurn();
//		testPieces();
//		firstMove = new Move(((Piece)field[2][3]));
//		firstMove.setTo(3, 2);
//		MoveToAction.UserAction(firstMove);
//		testPieces();
////		firstMove = new Move(((Piece)field[6][5]));
////		firstMove.setTo(4, 3);
////		MoveToAction.UserAction(firstMove);
////		testPieces();
////		firstMove = new Move(((Piece)field[4][3]));
////		firstMove.setTo(2, 1);
////		MoveToAction.UserAction(firstMove);
//		
//		MoveToAction.AIAction(((Piece)field[6][5]).getMoves().get(0));
//		
//		testPieces();
//	}
	
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
		
		if(endOfGame) { //TODO game reset
			System.out.println("end of game");
			gameOver = true;
//			startNewGame();
//			newGame();
//			calcBlackMoves();
//			Main.board = new Visual();
//			Main.board.place_pieces();
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
	
	public static void addPlaceholders (Piece piece) {
		List<Move> holdlist = piece.getMoves();
		Piece placepiece = new PlaceholderPiece();
		for (int i = 0; i <= holdlist.size() - 1; i++) {
//			System.out.println("place");
			int[] start = piece.getLocation();
			int[] coor = piece.getMoves().get(i).getToList().get(0);
			field[coor[0]][coor[1]] = placepiece.clone();
			((Piece)field[coor[0]][coor[1]]).setLocation(coor[0],coor[1]);
			((PlaceholderPiece)field[coor[0]][coor[1]]).setFrom(start[0], start[1]);
			Main.board.getChildren().add((PlaceholderPiece)field[coor[0]][coor[1]]);
		}
	}
	
	public static void removePlaceholders() {
		for(int i = 0; i <= field.length - 1; i++) {
			for (int j = 0; j <= field[0].length - 1; j++) {
				if (field[i][j] != null) {
					if (((Piece)field[i][j]).getColour() == 2) {
						Main.board.getChildren().remove((PlaceholderPiece)field[i][j]);
						field[i][j] = null;
					}
				}
			}
		}
	}
	
	public static void startNewGame() {
		for(int i = 0; i <= field.length - 1; i++) {
			for (int j = 0; j <= field[0].length - 1; j++) {
				if (field[i][j] != null) {
					if (((Piece)field[i][j]).getColour() == 2) {
						Main.board.getChildren().remove((PlaceholderPiece)field[i][j]);
					}
					else if (((Piece)field[i][j]).getColour() == 1) {
						Main.board.getChildren().remove((WhitePiece)field[i][j]);
					}
					else {
						Main.board.getChildren().remove((BlackPiece)field[i][j]);
					}
				}
			}
		}
	}
	
	public static Object[][] deepBoardCopy(Object[][] board){
		Object[][] boardCopy = new Object[board.length][board[0].length];
		
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if(board[i][j] == null) {
					boardCopy[i][j] = null;
				}
				else if (((Piece)board[i][j]).getColour() == 1) {
					WhitePiece hold = new WhitePiece();
					boardCopy[i][j] = hold.deepClone((Piece)(board[i][j]));
				}
				else {
					BlackPiece hold = new BlackPiece();
					boardCopy[i][j] = hold.deepClone((Piece)(board[i][j]));
				}
			}
		}
		
		return boardCopy;
	}
}
