package Gamecontroller;

import java.util.ArrayList;
import java.util.List;

import AIClasses.AIController;
import Board.Board;
import Move.Move;
import Move.MoveCalcTree;
import Move.MoveToAction;
import ObjectUI.Main;
import ObjectUI.Visual;
import Piece.BlackPiece;
import Piece.CheckersPiece;
import Piece.Piece;
import Piece.PlaceholderPiece;
import Piece.WhitePiece;
import TrueMinimax.TrueMinimaxTree;

public class Gamecontroller {
	public static Board board;
	public static Object[][] field;
	public static int turn = 0;
	public static final int MAX_TURNS = 100;
	public static boolean Visuals = true;
	public static boolean playerOneAI = false;
	public static boolean playerTwoAI = true;
	public static boolean gameOver = false;
	public static boolean gameDraw = false;
	public static boolean playerOneWon = false;
	public static List<Move> madeMoves = new ArrayList();
	
	// 0 = Random / 1 = MCTS / 2 = MINIMAX depth 1 / 3 = MINIMAX depth 2/ 4 = MINIMAX depth 3 /etc
	public static int AIone = 1;
	public static int AItwo = 1;
	
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
		gameOver = false;
		playerOneWon = false;
		gameDraw = false;
		madeMoves = new ArrayList();
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
						if(Gamecontroller.illegalMove(holdMoveList.get(k), madeMoves)) {
							((Piece) field[i][j]).getMoves().remove(k);
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
//			System.out.println("Black's turn");
		}
		else {
			calcWhiteMoves();
//			System.out.println("White's turn");
		}
		
		int amountBlack = 0;
		int amountWhite = 0;
		
		boolean endOfGame = true;
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 0) {
						amountWhite++;
					}
					else {
						amountBlack++;
					}
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
//			System.out.println("end of game");
			gameOver = true;
			if(amountWhite > 0 && amountBlack > 0) {
//				System.out.println("no more moves draw");
				gameDraw = true;
			}
			else {
				if((turn-1)%2 == 0) {
					playerOneWon = true;
//					System.out.println("one won");
				}
				else {
//					System.out.println("two won");
				}
			}
			
//			startNewGame();
//			newGame();
//			calcBlackMoves();
//			Main.board = new Visual();
//			Main.board.place_pieces();
		}
		
//		System.out.println("turn: " + turn);
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
						if(Gamecontroller.illegalMove(holdMoveList.get(k), madeMoves)) {
							((Piece) field[i][j]).getMoves().remove(k);
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
			
			if(field[coor[0]][coor[1]] == null) {
				field[coor[0]][coor[1]] = placepiece.clone();
				((Piece)field[coor[0]][coor[1]]).setLocation(coor[0],coor[1]);
				((PlaceholderPiece)field[coor[0]][coor[1]]).setFrom(start[0], start[1]);
				Main.board.getChildren().add((PlaceholderPiece)field[coor[0]][coor[1]]);
			}
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
	
	public static void AddMoveToList(Move m) {
		madeMoves.add(m);
		if(madeMoves.size() > 16) {
			madeMoves.remove(0);
		}
	}
	
	public static List<Move> copyMoveList(List<Move> MadeMoves){
		List<Move> returnList = new ArrayList();
		for(int i = 0; i <= MadeMoves.size()-1; i++) {
			returnList.add(MoveCalcTree.copyMove(MadeMoves.get(i)));
		}
		return returnList;
	}
	
	public static boolean illegalMove(Move m, List<Move> MadeMoves) {
		int count = 0;
		if(m.getRemoveList().size() == 0) {
			int[] moveEnd = m.getToList().get(0);
			for(int i = 0; i<= MadeMoves.size() - 1; i++) {
				if(MadeMoves.get(i).getRemoveList().size() == 0 && MadeMoves.get(i).getPiece().getColour() == m.getPiece().getColour()) {
					int[] MadeMoveEnd = MadeMoves.get(i).getToList().get(0);
					if((moveEnd[0] == MadeMoveEnd[0]) && (moveEnd[1] == MadeMoveEnd[1])) {
						count++;
					}
				}
			}
		}
		
		return (count > 2);
	}
	
	public static void gameLoop(double[] P1, double[] P2, int P1depth, int P2depth) {
		AIone = P1depth;
		AItwo = P2depth;
		while (!gameOver && turn < MAX_TURNS) {
			makeMove(P1, P2, P1depth, P2depth);
		}
//		testBoard();
	}
	
	public static void makeMove(double[] P1, double[] P2, int P1AI, int P2AI) {
		//TODO 
//		System.out.println(turn);
		if(turn%2 == 0) {
			if(P1AI == 0) {
				Move aiMove = AIController.getRandomMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				MoveToAction.AIAction(aiMove);
			}
			else if(P1AI == 1) {
				Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field), Gamecontroller.copyMoveList(Gamecontroller.madeMoves));
				MoveToAction.AIAction(aiMove);
			}
			else {
				AIController.weights = P1;
				TrueMinimaxTree.MAX_DEPTH = P1AI - 1;
				Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field), Gamecontroller.copyMoveList(Gamecontroller.madeMoves));
				MoveToAction.AIAction(aiMove);
			}
		}
		else {
			if(P2AI == 0) {
				Move aiMove = AIController.getRandomMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
				MoveToAction.AIAction(aiMove);
			}
			else if(P2AI == 1) {
				Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field), Gamecontroller.copyMoveList(Gamecontroller.madeMoves));
				MoveToAction.AIAction(aiMove);
			}
			else {
				AIController.weights = P2;
				TrueMinimaxTree.MAX_DEPTH = P2AI - 1;
				Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field), Gamecontroller.copyMoveList(Gamecontroller.madeMoves));
				MoveToAction.AIAction(aiMove);
			}
		}
	}
}
