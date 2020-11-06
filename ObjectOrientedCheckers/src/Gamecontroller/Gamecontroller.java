package Gamecontroller;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Move.Move;
import Piece.Piece;
import UI.BoardPanel;

public class Gamecontroller {
	public static Board board;
	public static Object[][] field;
	public static int turn = 0;
	
	public static void main(String[] args) {
		newGame();
		//testBoard();
		//gameLoop();
		calcBlackMoves();
		turn++;
		calcWhiteMoves();
		testPieces();	
	}
	
	public static void newGame() {
		board = new Board();
		field = board.getBoard();
		turn = 0;
	}
	
	public static void gameLoop() {
		if(turn%2 == 0) {
			calcBlackMoves();
		}
		else {
			calcWhiteMoves();
		}
		turn++;
		//gameLoop();
	}
	
	public static void calcBlackMoves() {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 0) {
						((Piece) field[i][j]).calcMoves();
					}
				}
			}		
		}
	}
	
	public static void calcWhiteMoves() {
		for(int i = 0; i <= field.length - 1; i++) {
			for(int j = 0; j <= field[0].length - 1; j++) {
				if(field[i][j] != null) {
					if(((Piece) field[i][j]).getColour() == 1) {
						((Piece) field[i][j]).calcMoves();
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
	}	

}
