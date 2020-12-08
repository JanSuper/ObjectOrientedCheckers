package Board;

import Piece.*;
import ObjectUI.SmartGroup;

public class Board{
	
	public static Object[][] board = new Object[8][8];
	
	public Board() {
		Piece whitePiece = new WhitePiece();
		Piece blackPiece = new BlackPiece();
		for(int i = 0; i <= board.length - 1; i++) {
			for(int j = 0; j <= board[0].length - 1; j++) {
				if((i%2==1 && j%2==0)||(i%2==0 && j%2==1)) {
					if(i <= 2) { 
						board[i][j] = blackPiece.clone(); 
						((Piece) board[i][j]).setLocation(i,j);
					}
					else if(i >= 5) { 
						board[i][j] = whitePiece.clone(); 
						((Piece) board[i][j]).setLocation(i,j);
					}
					else {
						board[i][j] = null;
					}
				}
				else {
					board[i][j] = null;
				}
			}
		}
	}
	
	public static Object[][] getBoard(){
		return board;
	}

}
